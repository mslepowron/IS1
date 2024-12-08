import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterLink, RouterModule } from '@angular/router';
import { Product } from '../services/product.service';
import { CartService } from '../services/cart.service';
import { ProductService } from '../services/product.service';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [RouterModule, RouterLink, CommonModule],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css'
})
export class CartComponent implements OnInit {
  cartItems: { product: Product; quantity: number }[] = [];
  products: any;

  constructor(private cartService: CartService, private productService: ProductService) { }

  ngOnInit(): void {
    this.loadCart();
  }

  // Cargar los productos del carrito
  loadCart(): void {
    this.cartItems = this.cartService.getCartItems();
  }

  // Incrementar la cantidad de un producto en el carrito
  increment(productId: number): void {
    const cartItem = this.cartItems.find(item => item.product.id === productId);
    if (!cartItem) return;

    // Verificar el stock antes de incrementar
    this.productService.getProductById(productId).subscribe({
      next: (product) => {
        if (cartItem.quantity < product.stock) {
          this.cartService.incrementQuantity(productId);
          this.loadCart(); // Recargar los datos actualizados
        } else {
          alert(`No hay suficiente stock disponible para ${product.name}.`);
        }
      },
      error: (err) => {
        console.error(`Error al obtener el producto con ID ${productId}:`, err);
        alert('Error al verificar el stock. Por favor, inténtalo más tarde.');
      }
    });
  }

  // Decrementar la cantidad de un producto en el carrito
  decrement(productId: number): void {
    this.cartService.decrementQuantity(productId);
    this.loadCart();
  }

  // Eliminar un producto del carrito
  remove(productId: number): void {
    this.cartService.removeFromCart(productId);
    this.loadCart();
  }

  // Crear una orden con los productos del carrito
  createOrder(): void {
    const token = localStorage.getItem('token');
    if (!token) {
      alert('You must be logged in to place an order.');
      return;
    }
  
    this.cartService.createOrder(token).subscribe({
      next: (response) => {
        alert('Order created successfully!');
        console.log('Order created:', response);
        this.cartService.clearCart();
        this.loadCart();
      },
      error: (err) => {
        console.error('Error creating order:', err);
        alert(err);
      }
    });
  }    
}
