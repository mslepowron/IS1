import { CommonModule } from '@angular/common';
import { Component, NgZone, OnInit } from '@angular/core';
import { Router, RouterLink, RouterModule } from '@angular/router';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-choose-avatar',
  standalone: true,
  imports: [],
  templateUrl: './choose-avatar.component.html',
  styleUrl: './choose-avatar.component.css'
})

export class ChooseAvatarComponent implements OnInit {
  userId: string | undefined;

  user: {
    name: string;
    surname: string;
    email: string;
    profilePicture: string;
    gender: string;
    age: string;
    address: string;

  } = {
      name: '',
      surname: '',
      email: '',
      profilePicture: '',
      gender: '',
      age: '',
      address: '',
    };

  constructor(private userService: UserService, private router: Router, private ngZone: NgZone) {
    (window as any).selectAvatar = this.selectAvatar.bind(this);
  }

  ngOnInit(): void {
    this.loadUserData();
  }

  // Carga los datos del usuario desde el backend
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
              this.user.name = response.name || undefined;
              this.user.surname = response.surname || undefined;
              this.user.email = response.email || undefined;
              this.user.age = response.age || undefined;
              this.user.gender = response.gender || undefined;
              this.user.address = response.address || undefined;
              this.user.profilePicture = undefined;
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

  // Función que se llama cuando el usuario selecciona un avatar
  selectAvatar(avatarUrl: string): void {
    const token = localStorage.getItem('token');

    if (token) {
      try {
        const decodedToken = JSON.parse(atob(token.split('.')[1])); // Decodificar el token
        const userId = decodedToken.sub; // Obtener el ID del usuario desde el token

        const userData = {
          name: this.user.name || '',
          surname: this.user.surname || '',
          age: this.user.age || '',
          gender: this.user.gender || '',
          address: this.user.address || '',
          profilePicture: avatarUrl,
        };

        if (userId) {
          // Llamar al backend para actualizar la foto de perfil
          this.userService.updateUser(userId, userData).subscribe({
            next: (response) => {
              console.log('Foto de perfil actualizada exitosamente', response);
              alert('¡Foto de perfil actualizada!');
              this.ngZone.run(() => this.router.navigate(['/user']));
            },
            error: (err) => {
              console.error('Error al actualizar la foto de perfil:', err);
              alert('Hubo un problema al actualizar la foto de perfil.');
            },
          });
        }
      } catch (error) {
        console.error('Error decodificando el token:', error);
      }
    }
  }
}
