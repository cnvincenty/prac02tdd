import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Modal } from 'bootstrap';
import { Producto } from '../dominio/producto';
import { Grupoproducto } from '../../grupoproducto/dominio/grupoproducto';
import { ProductoServicio } from '../servicio/producto-servicio';
import { GrupoproductoServicio } from '../../grupoproducto/servicio/grupoproducto-servicio';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-producto-componente',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './producto-componente.html',
  styleUrl: './producto-componente.css'
})
export class ProductoComponente implements OnInit{

  @ViewChild('ventanaModal') ventanaModalRef!: ElementRef; modal?: Modal;

  datos: Producto[] = [];
  dato: Producto = this.crearProductoVacio();
  editando = false;

  gruposproductos: Grupoproducto[] = [];

  constructor(
    private servicio: ProductoServicio,
    private grupoproductoService: GrupoproductoServicio
  ) { }

  ngOnInit(): void {
    this.cargarDatos();
    this.cargarCombos();
  }

  crearProductoVacio(): Producto {
    return {
      nombre: '',
      unidadMedida: '',
      preciounitario: 0,
      grupoproductoId: 0
    };
  }

  abrirNuevo(): void {
    this.dato = this.crearProductoVacio();
    this.editando = false;
    if (!this.modal) {
      this.modal = new Modal(this.ventanaModalRef.nativeElement);
    }
    this.modal.show();
  }

  cargarDatos(): void {
    this.servicio.obtenerTodos().subscribe(data => this.datos = data);
  }

  guardar(): void {
    const obs = this.editando && this.dato.id
      ? this.servicio.actualizar(this.dato)
      : this.servicio.crear(this.dato);

    obs.subscribe(() => {
      this.reset();
      this.cargarDatos();
      this.cerrarModal();
    });
  }

  editar(producto: Producto): void {
    this.dato = { ...producto };
    this.editando = true;
    if (!this.modal) {
      this.modal = new Modal(this.ventanaModalRef.nativeElement);
    }
    this.modal.show();
  }

  eliminar(id: number): void {
    if (confirm('¿Deseas eliminar este producto?')) {
      this.servicio.eliminarPorId(id).subscribe(() => this.cargarDatos());
    }
  }

  reset(): void {
    this.dato = this.crearProductoVacio();
    this.editando = false;
  }

  cerrarModal(): void {
    this.modal?.hide();
    this.reset();
  }

  cargarCombos(): void {
    this.grupoproductoService.obtenerTodos().subscribe(data => this.gruposproductos = data);
  }
}
