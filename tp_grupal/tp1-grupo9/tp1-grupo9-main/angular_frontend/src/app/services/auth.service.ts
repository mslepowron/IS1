import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  
  private baseUrl = '/auth'; // Usa el puerto de tu backend

  constructor(private http: HttpClient) { }

  // Función para registrar un nuevo usuario
  signup(registerData: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/signup`, registerData).pipe(
      catchError((error) => this.handleBackendError(error))
    );
  }

  login(email: string, password: string) {
    const loginData = { email, password };
    return this.http.post(`${this.baseUrl}/login`, loginData).pipe(
      catchError((error) => this.handleBackendError(error))
    );
  }

  // Manejo de errores directamente desde el backend
  private handleBackendError(error: HttpErrorResponse) {
    let errorMessage = 'An error occurred';

    // Si la respuesta del error tiene el objeto 'properties', accedemos a la descripción
    if (error.error?.properties?.description) {
      errorMessage = error.error.properties.description;
    }

    // Si no tiene el campo 'description', usa el 'detail' o algún otro valor que esté presente
    else if (error.error?.detail) {
      errorMessage = error.error.detail;
    }

    // Retornamos el error con el mensaje adecuado
    return throwError(() => new Error(errorMessage));
  }
}
