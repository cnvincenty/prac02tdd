import { HttpClient } from '@angular/common/http';
import { environment } from './../../../environments/environment.development';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Producto } from '../dominio/producto';

@Injectable({
  providedIn: 'root'
})
export class ProductoServicio {

  private readonly url = `${environment.apiUrl}/v1/producto`;

  constructor(private httpProducto: HttpClient) {}

  obtenerTodos(): Observable<Producto[]> {
    return this.httpProducto.get<Producto[]>(this.url);
  }

  obtenerPorId(id: number): Observable<Producto> {
    return this.httpProducto.get<Producto>(`${this.url}/${id}`);
  }

  crear(entrada: Producto): Observable<Producto> {
    return this.httpProducto.post<Producto>(this.url, entrada);
  }

  actualizar(entrada: Producto): Observable<Producto> {
    return this.httpProducto.put<Producto>(`${this.url}/${entrada.id}`, entrada);
  }

  eliminarPorId(id: number): Observable<Producto> {
    return this.httpProducto.delete<Producto>(`${this.url}/${id}`);
  }

}
