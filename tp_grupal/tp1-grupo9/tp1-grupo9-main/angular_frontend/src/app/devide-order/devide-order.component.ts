import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterLink, RouterModule } from '@angular/router';
import { CartService } from '../services/cart.service';
import { v4 as uuidv4 } from 'uuid';

let idCounter = 0; // Contador para asignar IDs únicos a los productos
@Component({
  selector: 'app-devide-order',
  standalone: true,
  imports: [RouterModule, RouterLink, CommonModule],
  templateUrl: './devide-order.component.html',
  styleUrls: ['./devide-order.component.css'],
})
export class DevideOrderComponent implements OnInit {
  suborders = [
    { items: [] },
  ];

  draggedItem: { id: string; name: string; quantity: number } | null = null;
  constructor(private cartService: CartService, private router: Router) {}

  ngOnInit() {
    this.loadCartItems();
  }

  // Carga los items del carrito en la primera suborden
  loadCartItems(): void {
    const cartItems = this.cartService.getProductsForOrder();
    if (cartItems.length > 0) {
      this.suborders[0].items = cartItems.flatMap((item) => {
        const items = [];
        for (let i = 0; i < item.quantity; i++) {
          items.push({
            id: item.product.id, // Crear un ID único para cada ítem
            name: item.product.name,
            quantity: 1, // Cada ítem se representa con cantidad 1
            product: item.product
          });
        }
        return items;
      });
    }
  }

  // Trigger que se ejecuta al iniciar el arrastre de un ítem
  onDragStart(event: DragEvent, item: { id: string; name: string; quantity: number }) {
    this.draggedItem = item; // Store the dragged item
    event.dataTransfer?.setData('text/plain', item.id); // Set the item's ID in the data transfer
  }

  // Allow drop by preventing default behavior
  onDragOver(event: DragEvent) {
    event.preventDefault();
  }

  // Handle drop event
  onDrop(event: DragEvent, suborderIndex: number) {
    event.preventDefault();
    const itemId = event.dataTransfer?.getData('text/plain'); // Get the item's ID from the data transfer

    // If the item exists, move it to the target suborder
    if (itemId && this.draggedItem) {
      const itemIndex = this.findItemIndex(this.draggedItem.id);
      if (itemIndex.suborderIndex !== suborderIndex) {
        // Remove item from current suborder
        this.suborders[itemIndex.suborderIndex].items.splice(itemIndex.itemIndex, 1);
        // Add item to the target suborder
        this.suborders[suborderIndex].items.push(this.draggedItem);
      }
      this.draggedItem = null; // Clear the dragged item
    }
  }

  // Find the item's index in the suborders
  findItemIndex(itemId: string): { suborderIndex: number; itemIndex: number } {
    for (let suborderIndex = 0; suborderIndex < this.suborders.length; suborderIndex++) {
      const itemIndex = this.suborders[suborderIndex].items.findIndex((item) => item.id === itemId);
      if (itemIndex !== -1) {
        return { suborderIndex, itemIndex };
      }
    }
    return { suborderIndex: -1, itemIndex: -1 };
  }

  // Add a new empty suborder
  addSuborder() {
    this.suborders.push({ items: [] });
  }

  deleteSuborder(index: number) {
    this.suborders.splice(index, 1);
  }
    
  submitSuborder(suborderIndex: number): void {
    const token = localStorage.getItem('token');
    if (!token) {
      alert('You must be logged in to place an order.');
      return;
    }
  
    const suborder = this.suborders[suborderIndex];
    if (!suborder || suborder.items.length === 0) {
      alert('The suborder must contain at least one item.');
      return;
    }
  
    // Verificación más detallada de productos
    const suborderItems = suborder.items.map(item => {  
      // Verificar que product sea un objeto válido
      if (item.product && typeof item.product === 'object' && item.product.hasOwnProperty('id') && item.product.id) {
        return {
          product: item.product,  // Asegurarse de enviar el objeto completo del producto
          quantity: item.quantity
        };
      } else {
        return null;
      }
    }).filter(item => item !== null);  // Filtrar productos nulos
  
    if (suborderItems.length === 0) {
      alert('No valid products found for the suborder.');
      return;
    }
  
    // Llamar al servicio para crear la suborden
    this.cartService.createSuborder(token, suborderItems).subscribe(
      (response) => {
        alert('Suborder created successfully!');
        this.suborders[suborderIndex].items = [];
        this.cartService.clearCart();
      },
      (error) => {
        alert(error);
      }
    );
  }
  
}
