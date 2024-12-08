import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// Interfaz para mapear los datos del DTO
export interface Order {
  id: number;
  state: string;
  createdAt: string; // Formato de fecha en string: 'yyyy-MM-dd HH:mm:ss'
  username: string;
  products: Product[]; // Lista de productos en formato JSON
}

export interface Product {
  id: number;
  name: string;
  description: string; // Descripción del producto
  stock: number; // Stock del producto
  attributes: Record<string, string>; // Atributos del producto (clave-valor)
}

@Injectable({
  providedIn: 'root',
})
export class OrderService {

  // URL del backend
  private apiUrl = 'http://localhost:20002/orders';

  constructor(private http: HttpClient) { }

  // Método para obtener la lista de pedidos
  getOrders(token: string): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.apiUrl}/`, {
      headers: { Authorization: `Bearer ${token}` },
    });
  }

  getOrdersAdmin(token: string): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.apiUrl}/state/`, {
      headers: { Authorization: `Bearer ${token}` },
    });
  }

  // Método para obtener la lista de pedidos por estado
  getOrdersByState(token: string, state: string): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.apiUrl}/state/`, {
      headers: { Authorization: `Bearer ${token}` },
    });
  }

  // Método para obtener el stock de un producto
  updateProductStock(productId: number, updatedProduct: { stock: number }): Observable<any> {
    const token = localStorage.getItem('token');
    if (!token) {
      alert('Authentication token missing. Please log in again.');
      throw new Error('No authentication token found.');
    }

    console.log('Updating product stock:', updatedProduct); // LOG

    return this.http.patch(`${this.apiUrl.replace('/orders', '')}/products/${productId}`, updatedProduct, {
      headers: { Authorization: `Bearer ${token}` },
    });
  }

  // Método para actualizar el estado de una orden
  updateOrderState(token: string, orderId: number, newState: string): Observable<Order> {
    return this.http.patch<Order>(
      `${this.apiUrl}/order/?id=${orderId}`, 
      { state: newState },
      { headers: { Authorization: `Bearer ${token}` } }
    );
  }
}
