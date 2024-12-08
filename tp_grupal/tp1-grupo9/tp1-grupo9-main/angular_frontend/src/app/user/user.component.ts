import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterLink, RouterModule } from '@angular/router';
import { UserService } from '../services/user.service';
import { jwtDecode, JwtPayload } from 'jwt-decode';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [RouterModule, RouterLink, CommonModule],
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
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
      profilePicture: '',
    };

  email: string = '';
  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.checkUserData();
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

  // Función para obtener los datos del usuario
  checkUserData(): void {
    const token = localStorage.getItem('token');
    if (token) {
      try {
        const decodedToken = JSON.parse(atob(token.split('.')[1]));
        const userId = decodedToken.sub;

        if (userId) {
          this.userService.getUser(userId).subscribe({
            next: (response) => {
              this.user.firstName = response.name || '';
              this.user.lastName = response.surname || '';
              this.user.email = this.email || '';
              this.user.age = response.age || '';
              this.user.gender = response.gender || '';
              this.user.address = response.address || '';
              this.user.profilePicture = this.getProfileImage(response.profilePicture);
            },
            error: (err) => {
              console.error('Error al obtener los datos del usuario:', err);
              alert('Hubo un problema al obtener los datos. Por favor, intenta nuevamente.');
            }
          });
        }
      } catch (error) {
        console.error('Error al decodificar el token:', error);
      }
    }
  }

  // Función para obtener la imagen de perfil del usuario
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
}
