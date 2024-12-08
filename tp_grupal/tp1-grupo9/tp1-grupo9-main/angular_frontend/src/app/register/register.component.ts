import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  standalone: true,
  styleUrls: ['./register.component.css'],
  imports: [CommonModule, ReactiveFormsModule, FormsModule]
})

export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;
  showError: boolean = false;
  errorMessage: string = '';

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) { }

  // Inicializar el formulario de registro
  ngOnInit(): void {
    this.registerForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required],
      name: ['', Validators.required], // Nombre del usuario
      surname: ['', Validators.required], // Apellido del usuario
      profilePicture: ['assets/images/default-avatar.png'], // Imagen de perfil por defecto
    });
  }

  // Verificar si las contraseñas no coinciden
  get passwordsDoNotMatch(): boolean {
    const { password, confirmPassword } = this.registerForm.value;
    return password && confirmPassword && password !== confirmPassword;
  }

  // Enviar el formulario de registro
  onSubmit(): void {
    if (this.registerForm.invalid || this.passwordsDoNotMatch) {
      this.showError = true;
      this.errorMessage = 'Please check the form for errors.';
      return;
    }

    const registerData = {
      email: this.registerForm.value.email,
      password: this.registerForm.value.password,
      name: this.registerForm.value.name,
      surname: this.registerForm.value.surname,
      profilePicture: 'assets/images/default-avatar.png',
      gender: '',
      age: '',
      address: ''
    };

    // Llamar al servicio AuthService para registrar el usuario
    this.authService.signup(registerData).subscribe({
      next: (response) => {
        console.log('Registration successful:', response);
        this.router.navigate(['/']); // Redirigir al usuario después del registro exitoso
      },
      error: (err) => {
        console.error('Registration error:', err);
        this.showError = true;
        this.errorMessage = err.message || 'An error occurred during registration.';
      }
    });
  }
}

