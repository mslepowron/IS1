import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

export interface User {
  email: string;
  address: string;
  age: string;
  gender: string;
  username: string;
  profile_picture: string;
  surname: string;
}

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiUrl = 'http://localhost:20002'; // URL del backend

  constructor(private http: HttpClient) { }

  // Obtener los datos del usuario
  getUser(userId: string): Observable<any> {
    const token = localStorage.getItem('token'); // Obtener el token del almacenamiento local
    let headers = new HttpHeaders();

    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`); // Agregar el token en los encabezados
    }

    return this.http.get(`${this.apiUrl}/users/`, { headers });
  }

  // Actualizar los datos del usuario
  updateUser(userId: string, userData: Partial<User>): Observable<any> {
    const token = localStorage.getItem('token'); // Obtener el token del almacenamiento local
    let headers = new HttpHeaders();

    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`); // Agregar el token en los encabezados
    } else {
      console.error('No se encontró el token');
    }

    return this.http.patch(`${this.apiUrl}/users/`, userData, { headers }).pipe(
      catchError((error) => {
        console.error('Error al actualizar el usuario:', error);
        return throwError(() => new Error('Error al actualizar el usuario'));
      })
    );
  }

  // Obtener las órdenes de un usuario por email
  getOrdersByEmail(email: string): Observable<any> {
    const token = localStorage.getItem('token');
    let headers = new HttpHeaders();

    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }

    // Modificar la URL para incluir el email en la ruta
    return this.http.get(`${this.apiUrl}/orders/${email}`, { headers }).pipe(
      catchError((error) => {
        console.error('Error al obtener las órdenes:', error);
        return throwError(() => new Error('Error al obtener las órdenes'));
      })
    );
  }
}
