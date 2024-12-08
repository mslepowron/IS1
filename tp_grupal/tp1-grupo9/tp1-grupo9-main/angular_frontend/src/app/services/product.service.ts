import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Product {
  id?: number;
  name: string;
  description: string;
  stock: number;
  attributes?: Record<string, string>;
}

export interface CreateProduct {
  name: string;
  description: string;
  stock: number;
  attributes?: Record<string, string>;
}

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private apiUrl = 'http://localhost:20002'; // URL correcta del backend

  constructor(private http: HttpClient) { }

  getProducts(token: string): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/products/`, {
      headers: { Authorization: `Bearer ${token}` },
    });
  }

  createProduct(token: string, product: CreateProduct): Observable<Product> {
    return this.http.post<Product>(`${this.apiUrl}/products`, product, {
      headers: { Authorization: `Bearer ${token}` },
    });
  }

  getProductById(productId: number): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/products/${productId}`, {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
    });
  }
  
  updateProductStock(token: string, productId: number, newStock: number): Observable<Product> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.patch<Product>(
      `${this.apiUrl}/products/?id=${productId}`,
      { stock: newStock },
      { headers }
    );
  }
  
}
