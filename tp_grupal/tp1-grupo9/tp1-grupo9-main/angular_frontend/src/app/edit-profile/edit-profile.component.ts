import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterLink, RouterModule } from '@angular/router';
import { UserService } from '../services/user.service';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { jwtDecode, JwtPayload } from 'jwt-decode';

@Component({
  selector: 'app-edit-profile',
  standalone: true,
  imports: [RouterModule, RouterLink, CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css'],
})
export class EditProfileComponent implements OnInit {
  editProfileForm!: FormGroup;
  userId: string | undefined;

  user: {
    firstName: string;
    lastName: string;
    email: string;
    age: string;
    gender: string;
    address: string;
    profilePicture: string;
  } = {
      firstName: '',
      lastName: '',
      email: '',
      age: '',
      gender: '',
      address: '',
      profilePicture: ''
    };

  email: string = '';
  constructor(private fb: FormBuilder, private userService: UserService, private router: Router) { }

  ngOnInit(): void {
    this.initializeForm();
    this.loadUserData();
    this.checkUserEmail();
  }

  // Método para verificar el email del usuario
  checkUserEmail(): void {
    const token = localStorage.getItem('token');
    if (token) {
      try {
        const decodedToken: JwtPayload = jwtDecode<JwtPayload>(token);
        this.email = decodedToken.sub || ''; // Asignar el email del JWT
      } catch (error) {
        console.error('Error al decodificar el token:', error);
      }
    }
  }

  private initializeForm(): void {
    this.editProfileForm = this.fb.group({
      name: ['', Validators.required],
      surname: ['', Validators.required],
      email: [{ value: '', disabled: true }, [Validators.required, Validators.email]],
      profilePicture: [''],
      gender: [''],
      age: [''],
      address: [''],

    });
  }

  // Cargar los datos del usuario desde el backend
  loadUserData(): void {
    const token = localStorage.getItem('token');
    if (token) {
      try {
        const decodedToken = JSON.parse(atob(token.split('.')[1])); // Decodificar solo el token

        this.userId = decodedToken.sub; // Obtener el ID del usuario desde el token

        if (this.userId) {
          // Obtener los datos del usuario desde el backend
          this.userService.getUser(this.userId).subscribe({
            next: (response) => {
              console.log('Datos del usuario cargados:', response);
              // Asignar los datos del usuario a los campos
              this.editProfileForm.patchValue({
                name: response.name || undefined,
                surname: response.surname || undefined,
                email: this.email || undefined,
                profilePicture: this.getProfileImage(response.profilePicture),
                gender: response.gender || undefined,
                age: response.age || undefined,
                address: response.address || undefined,

              });
            },
            error: (err) => {
              console.error('Error al cargar los datos del usuario:', err);
              alert('Hubo un problema al cargar los datos. Por favor, intenta nuevamente.');
            },
          });
        }
      } catch (error) {
        console.error('Error decodificando el token:', error);
      }
    }
  }

  getProfileImage(profilePicture: string): string {
    const avatarMap: { [key: string]: string } = {
      avatar1: 'assets/images/avatar1.png',
      avatar2: 'assets/images/avatar2.png',
      avatar3: 'assets/images/avatar3.png',
      avatar4: 'assets/images/avatar4.png',
      avatar5: 'assets/images/avatar5.png',
      avatar6: 'assets/images/avatar6.png'
    };

    if (profilePicture && typeof profilePicture === 'string') {
      // Normaliza el valor de profilePicture a minúsculas y sin espacios
      const cleanedProfilePicture = profilePicture.trim().toLowerCase();

      // Busca el valor en el mapa
      const foundKey = Object.keys(avatarMap).find(
        key => avatarMap[key].toLowerCase() === cleanedProfilePicture
      );

      // Devuelve la clave encontrada o una URL predeterminada
      return foundKey ? avatarMap[foundKey] : 'assets/images/default-avatar.png';
    } else {
      return 'assets/images/default-avatar.png';
    }
  }


  saveChanges(): void {
    const userId = this.userId;

    if (!userId) {
      console.error('No se pudo obtener el ID del usuario');
      return;
    }

    const userData = {
      ...this.editProfileForm.value,
    };

    console.log('Datos a enviar:', userData); // Agrega un log para verificar los datos
    this.userService.updateUser(userId, userData).subscribe({
      next: (response) => {
        console.log('Datos actualizados exitosamente:', response);
        this.loadUserData();
      },
      error: (err) => {
        console.error('Error al actualizar los datos:', err);
        alert('Hubo un problema al guardar los datos. Por favor, intenta nuevamente.');
      },
    });
    this.router.navigate(['/user']);
  }

  onCancel(): void {
    this.router.navigate(['/user']);
  }
}
