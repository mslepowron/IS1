import { Component, OnInit } from '@angular/core';
import { Product, ProductService } from '../services/product.service';
import { jwtDecode } from 'jwt-decode';
import { CartService } from '../services/cart.service';
import { CommonModule } from '@angular/common';
import { RouterLink } from "@angular/router";

interface JwtPayload {
  sub: string; // email
  role: string; // Rol del usuario
  exp: number; // Expiración
  [key: string]: any; // Otros datos adicionales en el JWT
}

@Component({
  selector: 'app-shop',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './shop.component.html',
  styleUrls: ['./shop.component.css']
})
export class ShopComponent implements OnInit {
  products: Product[] = [];
  filteredProducts: Product[] = [];
  isAdmin: boolean = false;
  selectedProduct: any;
  cart: any[] = [];

  // Método para obtener las llaves de un objeto
  getObjectKeys(obj: Record<string, string>): string[] {
    return Object.keys(obj);
  }

  constructor(
    private productService: ProductService,
    private cartService: CartService 
  ) { }

  // Método para inicializar el componente
  ngOnInit(): void {
    this.loadProducts();
    this.checkUserRole();
  }

  // Método para verificar el rol del usuario
  checkUserRole(): void {
    const token = localStorage.getItem('token');
    if (token) {
      try {
        const decodedToken: JwtPayload = jwtDecode<JwtPayload>(token);
        this.isAdmin = decodedToken.role === 'ROLE_ADMIN'; // Verifica si el rol es ADMIN y segun eso se habilitan las funcionalidades de admin
        console.log('Decoded JWT:', decodedToken);
      } catch (error) {
        console.error('Error al decodificar el token:', error);
      }
    }
  }

  // Método para cargar los productos
  loadProducts(): void {
    const token = localStorage.getItem('token'); // Obtengo el token almacenado en localStorage
    console.log('Token:', token);

    if (token) {
      this.productService.getProducts(token).subscribe({
        next: (data) => {
          console.log('Datos recibidos:', data);
          this.products = data;
          this.filteredProducts = [...this.products];
        },
        error: (error) => {
          console.error('Error al cargar los productos:', {
            status: error.status,
            message: error.message,
            body: error.error,
          });
        },
      });
    } else {
      console.error('No se encontró el token');
    }
  }

  // Método para filtrar productos
  filterProducts(searchQuery: string): void {
    const query = searchQuery.toLowerCase().trim();
    this.filteredProducts = this.products.filter((product) =>
      product.name.toLowerCase().includes(query)
    );
  }

  // Método para añadir al carrito
  addToCart(product: Product): void {
    this.cartService.addToCart(product);
    alert(`${product.name} added to your cart.`);
  }

  // Método para mostrar los detalles del producto
  showProductDetails(product: Product): void {
    console.log('Producto seleccionado:', product);
    this.selectedProduct = product;
  }

  // Método para cerrar el detalle del producto
  flipCard() {
    this.selectedProduct = null;
  }

  // Método para verificar si un producto está en el carrito
  isInCart(product: Product): boolean {
    return this.cartService.getCartItems().some(item => item.product.id === product.id);
  }

  // Método para obtener la cantidad actual de un producto en el carrito
  getCartQuantity(product: Product): number {
    const item = this.cartService.getCartItems().find(item => item.product.id === product.id);
    return item ? item.quantity : 0;
  }

  // Métodos para incrementar y decrementar la cantidad
  increaseQuantity(product: Product): void {
    const cartItems = this.cartService.getCartItems();
    const item = cartItems.find(item => item.product.id === product.id);

    if (item && item.quantity < product.stock) {
      this.cartService.incrementQuantity(product.id!);
    } else {
      alert(`No more stock available for ${product.name}`);
    }
  }

  // Método para disminuir la cantidad de un producto en el carrito
  decreaseQuantity(product: Product): void {
    this.cartService.decrementQuantity(product.id!);
  }
}
