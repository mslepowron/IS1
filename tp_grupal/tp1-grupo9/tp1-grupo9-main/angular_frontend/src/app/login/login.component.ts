import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { RouterLink } from '@angular/router';
import { Router } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [RouterLink, FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  email: string = '';
  password: string = '';

  constructor(private authService: AuthService, private router: Router) { }

  login() {
    if (!this.email || !this.password) {
      alert('Please fill in both email and password.');
      return;
    }

    // Simulación de login exitoso
    this.authService.login(this.email, this.password).subscribe({
      next: (response: any) => {
        // Guardar token o realizar acciones en caso de éxito
        localStorage.setItem('token', response.token);
        localStorage.setItem('email', this.email);
        console.log('Logged in successfully:', response);
        alert('Login successful!');
        this.router.navigate(['/shop']);
      },
      error: (err) => {
        // Manejar errores (usuario no encontrado, contraseña incorrecta, etc.)
        alert(err.message);
      }
    });

  }

  register() {
    this.router.navigate(['/register']);
  }
}

