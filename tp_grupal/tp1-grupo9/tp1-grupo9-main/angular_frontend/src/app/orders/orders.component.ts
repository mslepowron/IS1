import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterLink, RouterModule } from '@angular/router';
import { UserService } from '../services/user.service';
import { OrderService, Order } from '../services/order.service';

@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [RouterModule, RouterLink, CommonModule],
  templateUrl: './orders.component.html',
  styleUrl: './orders.component.css'
})
export class OrdersComponent implements OnInit {
  orders: any[] = [];
  token: string | null = null;
  email: string | null = null;

  constructor(private orderService: OrderService) { }

  ngOnInit(): void {
    this.token = localStorage.getItem('token'); 
    this.email = localStorage.getItem('email'); 
    if (this.token) {
      // Llama al servicio para obtener las órdenes del usuario autenticado
      this.orderService.getOrders(this.token).subscribe({
        next: (data) => {
          this.orders = data; // Asigna las órdenes devueltas por el backend
        },
        error: (err) => {
          console.error('Error al cargar las órdenes:', err);
        },
      });
    } else {
      console.error('No se encontró el token del usuario');
    }
  }

  // Método para verificar si la orden puede ser cancelada
  validateCancelAvailability(order: Order): boolean {
    // Obtener la fecha actual
    const now = new Date();

    // Convertir la fecha de creación de la orden en objeto Date (suponiendo que createdAt está en formato ISO)
    const orderCreatedAt = new Date(order.createdAt);

    // Calcular la diferencia de tiempo en horas
    const hoursDifference = (now.getTime() - orderCreatedAt.getTime()) / (1000 * 3600); // Diferencia en horas

    // Si la diferencia es mayor a 24 horas, no se puede cancelar
    return hoursDifference <= 24 && order.state != 'CANCELED' && order.state != 'PROCESSING' && order.state != 'SENT';
  }

  // Método para cancelar la orden
  cancelOrder(orderId: number): void {
    const order = this.orders.find(o => o.id === orderId);

    if (order && this.validateCancelAvailability(order)) {
      // Si la validación pasa, se puede cancelar la orden
      console.log(`Order ${orderId} can be cancelled.`);
      this.changeOrderState(orderId, 'CANCELED');
    } else {
      // Si no pasa la validación, informar al usuario
      console.log(`Order ${orderId} cannot be cancelled because more than 24 hours have passed.`);
    }
  }

  // Método para cambiar el estado de la orden
  changeOrderState(orderId: number, newState: string): void {
    if (this.token && this.email) {
      this.orderService.updateOrderState(this.token, orderId, newState).subscribe({ 
        next: (updatedOrder) => {
          const index = this.orders.findIndex((order) => order.id === orderId);
          if (index !== -1) {
            this.orders[index].state = newState;
          }
        },
        error: (err) => {
          console.error('Error updating order state:', err);
        },
      });
    } else {
      console.error('No token found');
    }
  }
}