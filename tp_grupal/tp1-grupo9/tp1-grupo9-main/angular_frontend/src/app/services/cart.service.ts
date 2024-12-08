import { Injectable } from '@angular/core';
import { Product, ProductService } from '../services/product.service';
import { catchError, forkJoin, map, Observable, switchMap, throwError } from 'rxjs';
import { OrderService } from '../services/order.service';
import { BehaviorSubject } from 'rxjs';
import { HttpClient } from '@angular/common/http'; 

@Injectable({
  providedIn: 'root'
})
export class CartService {

  private cartKey = 'cart';
  private cart: { product: Product; quantity: number }[] = [];
  private cartSubject = new BehaviorSubject(this.cart);

  constructor(private orderService: OrderService, private productService: ProductService, private http: HttpClient) {
    this.loadCart();
  }

  // Crear una orden con los productos del carrito
  createOrder(token: string): Observable<any> {
    const headers = { Authorization: `Bearer ${token}` }; // Token JWT
  
    // Crear una lista de IDs donde cada ID aparece tantas veces como la cantidad en el carrito
    const productIds = this.cart.flatMap(item =>
      Array(item.quantity).fill(item.product.id)
    );
  
    // Crear el objeto OrderCreateDTO con la lista de IDs
    const orderData = { ids: productIds };
  
    // Hacer la solicitud POST para crear la orden
    return this.http.post('http://localhost:20002/orders', orderData, { headers }).pipe(
      catchError((error) => {
        console.error('Error creating order:', error);
        return throwError(() => new Error(error?.error?.detail || 'Error creating order'));
      })
    );
  }

  createSuborder(token: string, suborderItems: { product: Product; quantity: number }[]): Observable<any> {
    const headers = { Authorization: `Bearer ${token}` };
  
    // Verificar que todos los productos estén definidos
    const validItems = suborderItems.filter(item => item.product && item.product.id);
  
    if (validItems.length === 0) {
      return throwError(() => new Error('No valid products found for the suborder'));
    }
  
    // Crear la lista de IDs
    const productIds = validItems.flatMap(item =>
      Array(item.quantity).fill(item.product.id)
    );
  
    // Crear el objeto de suborden
    const suborderData = { ids: productIds };
  
    // Enviar la solicitud POST
    return this.http.post('http://localhost:20002/orders', suborderData, { headers }).pipe(
      catchError((error) => {
        console.error('Error creating suborder:', error);
        return throwError(() => new Error(error?.error?.detail || 'Error creating suborder'));
      })
    );
  }
  

    
  // Guardar el carrito en el almacenamiento local
  private saveCart(): void {
    localStorage.setItem(this.cartKey, JSON.stringify(this.cart));
    this.cartSubject.next(this.cart); // Notifica cambios al observable
  }

  // Cargar el carrito desde el almacenamiento local
  private loadCart(): void {
    const storedCart = localStorage.getItem(this.cartKey);
    if (storedCart) {
      this.cart = JSON.parse(storedCart);
      this.cartSubject.next(this.cart);
    }
  }
  
  // Agregar un producto al carrito
  addToCart(product: Product): void {
    const existingItem = this.cart.find(item => item.product.id === product.id);
    if (existingItem) {
      existingItem.quantity += 1;
    } else {
      this.cart.push({ product, quantity: 1 });
    }
    this.saveCart();
  }

  // Eliminar un producto del carrito
  removeFromCart(productId: number): void {
    this.cart = this.cart.filter(item => item.product.id !== productId);
    this.saveCart();
  }

  // Incrementar la cantidad de un producto en el carrito
  incrementQuantity(productId: number): void {
    const existingItem = this.cart.find(item => item.product.id === productId);
    if (existingItem) {
      existingItem.quantity += 1;
      this.saveCart();
    }
  }

  // Decrementar la cantidad de un producto en el carrito
  decrementQuantity(productId: number): void {
    const existingItem = this.cart.find(item => item.product.id === productId);
    if (existingItem) {
      if (existingItem.quantity > 1) {
        existingItem.quantity -= 1;
      } else {
        this.removeFromCart(productId);
      }
      this.saveCart();
    }
  }

  // Obtener los productos del carrito
  getCartItems(): { product: Product; quantity: number }[] {
    return this.cart;
  }

  // Obtener los productos del carrito como un observable
  getProductsForOrder(): { product: Product; quantity: number }[] {
    return [...this.cart]; // Devuelve una copia para evitar modificaciones accidentales
  }

  // Limpiar el carrito
  clearCart(): void {
    this.cart = [];
    this.saveCart();
  }
}
