import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.css'
})

export class ForgotPasswordComponent implements OnInit {
  forgotPasswordForm: FormGroup;
  showError: boolean = false;
  showSuccess: boolean = false;

  constructor(private fb: FormBuilder, private router: Router) {}

  ngOnInit(): void {
    this.forgotPasswordForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      repeatNewPassword: ['', [Validators.required]],
    });
  }

  get passwordsDoNotMatch(): boolean {
    const { newPassword, repeatNewPassword } = this.forgotPasswordForm.value;
    return newPassword && repeatNewPassword && newPassword !== repeatNewPassword;
  }

  onSubmit(): void {
    if (this.forgotPasswordForm.invalid || this.passwordsDoNotMatch) {
      this.showError = true;
      this.showSuccess = false;
      return;
    }

    this.showError = false;
    this.showSuccess = true;

    // Simula redirección tras éxito
    setTimeout(() => {
      this.router.navigate(['/']); // Cambia al destino deseado
    }, 1000);
  }
}
