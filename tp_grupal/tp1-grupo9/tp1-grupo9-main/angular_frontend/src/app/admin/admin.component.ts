import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterLink, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ProductService, Product } from '../services/product.service';
import { OrderService, Order } from '../services/order.service';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [RouterModule, RouterLink, CommonModule, FormsModule],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css'
})

export class AdminComponent implements OnInit {
  Object = Object;
  orders: Order[] = []; // Lista de pedidos
  activeTab: string = 'productos'; // Pestaña predeterminada
  products: Product[] = []; // Lista de productos
  token: string | null = null; // Token del usuario
  email: string | null = null;
  editableProduct: any = null; // Para rastrear el producto en edición

  // Estado del formulario de creación
  showCreateProductForm: boolean = false;
  newProduct: Product = {
    name: '',
    stock: 0,
    description: '',
    attributes: {},
  };

  attributeKey: string = '';
  attributeValue: string = '';

  constructor(private productService: ProductService, private orderService: OrderService) { }

  // Cambia la pestaña activa
  setActiveTab(tab: string): void {
    this.activeTab = tab; 
  }

  // Inicializa el componente
  ngOnInit(): void {
    this.token = localStorage.getItem('token');
    this.email = localStorage.getItem('email');
    //DECODIFICAR Y CHEQUEAR EL ROL. sino redireccionar a page not found
    if (this.token) {
      this.loadProducts(this.token);
      this.fetchOrders(this.token);
    } else {
      console.error('No token found');
    }
  }

  // Cargar los productos desde el backend
  loadProducts(token: string): void {
    this.productService.getProducts(token).subscribe({
      next: (products) => {
        this.products = products; // Asigna los productos al array de products
      },
      error: (err) => {
        console.error('Error loading products:', err);
      },
    });
  }

  // Activa o desactiva el formulario de creación de productos
  toggleCreateProductForm(): void {
    this.showCreateProductForm = !this.showCreateProductForm;
    if (!this.showCreateProductForm) {
      this.newProduct = { name: '', stock: 0, description: '', attributes: {} };
    }
  }

  // Resetea el formulario de creación de productos
  resetNewProduct(): void {
    this.newProduct = { name: '', stock: 0, description: '', attributes: {} };
    this.attributeKey = '';
    this.attributeValue = '';
  }

  // Agrega un atributo al producto
  addAttribute(): void {
    if (this.attributeKey.trim() && this.attributeValue.trim()) {
      this.newProduct.attributes[this.attributeKey] = this.attributeValue;
      this.attributeKey = '';
      this.attributeValue = '';
    }
  }

  // Crea un nuevo producto
  createProduct(): void {
    if (this.newProduct.name.trim() && this.newProduct.stock > 0 && this.newProduct.description.trim()) {
      if (this.token) {
        this.productService.createProduct(this.token, this.newProduct).subscribe({
          next: (createdProduct) => {
            this.products.push(createdProduct);
            this.newProduct = { name: '', stock: 0, description: '', attributes: {} };
            this.showCreateProductForm = false;
          },
          error: (err) => {
            console.error('Error creating product:', err);
          },
        });
      } else {
        console.error('No token found');
      }
    } else {
      alert('Please provide valid product details.');
    }
  }

  // Activa la edición de un producto
  editProductStock(product: Product): void {
    this.editableProduct = { ...product, newStock: product.stock };
  }

  // Cancela la edición
  cancelEdit(): void {
    this.editableProduct = null;
  }

  // Actualiza el stock de un producto
  updateStock(): void {
    if (this.token && this.editableProduct?.id != null && this.editableProduct.newStock >= 0) {
      this.productService
        .updateProductStock(this.token, this.editableProduct.id, this.editableProduct.newStock)
        .subscribe({
          next: (updatedProduct) => {
            // Actualiza la lista de productos con el producto actualizado
            const index = this.products.findIndex((p) => p.id === updatedProduct.id);
            if (index !== -1) {
              this.products[index] = updatedProduct;
            }
            this.editableProduct = null; // Resetea el formulario de edición
          },
          error: (err) => {
            console.error('Error updating product stock:', err);
          },
        });
    } else {
      alert('Invalid stock value.');
    }
  }

  // Cargar los pedidos desde el backend
  loadOrders(): void {
    this.setActiveTab('pedidos'); // Establece la tab activa
    this.fetchOrders(this.token); // Llama al back
  }

  // Obtiene los pedidos del usuario desde el backend
  fetchOrders(token: string): void {
    if (this.token) {
      this.orderService.getOrdersAdmin(this.token).subscribe({
        next: (orders) => {
          console.log('Orders:', orders);
          this.orders = orders;
        },
        error: (err) => {
          console.error('Error fetching orders:', err);
        },
      });
    } else {
      console.error('No token found');
    }
  }

  // Cambia el estado de un pedido
  changeOrderState(orderId: number, newState: string): void {
    const order = this.orders.find(o => o.id === orderId);

    // Verificar que el estado actual sea "CONFIRMED" antes de permitir la actualización
    if (order && (order.state === 'CONFIRMED' || order.state === 'PROCESSING')) {
      // Asegúrate de que el nuevo estado sea válido para el cambio
      if (['CANCELED', 'PROCESSING', 'SENT'].includes(newState)) {
        if (this.token) {
          this.orderService.updateOrderState(this.token, orderId, newState).subscribe({
            next: (updatedOrder) => {
              // Actualiza el estado en la lista de pedidos
              const index = this.orders.findIndex((order) => order.id === updatedOrder.id);
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
      } else {
        alert('Invalid state transition');
      }
    } else {
      alert('Order must be in CONFIRMED state to change');
    }
  }

}