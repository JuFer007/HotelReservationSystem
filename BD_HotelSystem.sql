create database HotelSystem;
use HotelSystem;

-- TABLA TIPO HAB.
create table TipoHabitacion(
	idTipoHabitacion int primary key auto_increment,
    descripcion varchar(200) not null,
    precio decimal(10,2) not null
);

-- TABLA EMPLEADO
create table Empleado (
	idEmpleado int primary key auto_increment,
    nombre varchar(50) not null,
    apellidoPaterno varchar(50) not null,
    apellidoMaterno varchar(50),
    dni varchar(15) not null unique,
    telefono varchar(15) not null
);

-- TABLA USUARIO
create table Usuario(
	idUsuario int primary key auto_increment,
    idEmpleado int not null unique,
    nombreUsuario varchar(50) not null unique,
    contraseña varchar(100) not null,
    foreign key (idEmpleado) references Empleado(idEmpleado)
);

-- TABLA RECEPCIONISTA
create table Recepcionista(
	idRecepcionista int primary key auto_increment,
    idEmpleado int not null unique,
    turnoTrabajo varchar(20) not null,
    foreign key (idEmpleado) references Empleado(idEmpleado)
);

-- TABLA ADMIN
create table Administrador(
	idAdministrador int primary key auto_increment,
    idEmpleado int not null unique,
    correoElectronico varchar(100) not null,
    foreign key (idEmpleado) references Empleado(idEmpleado)
);

-- TABLA CLIENTE
create table Cliente(
	idCliente int primary key auto_increment,
    nombre varchar(50) not null,
    apellidoPaterno varchar(50) not null,
    apellidoMaterno varchar(50),
    dni varchar(15) not null unique,
    telefono varchar(15) not null,
    email varchar(100)
);

-- TABLA HAB.
create table Habitacion(
	idHabitacion int primary key auto_increment,
    idTipoHabitacion int not null,
    numeroHabitacion varchar(10) not null unique,
    estado varchar(20) not null,
    foreign key (idTipoHabitacion) references TipoHabitacion(idTipoHabitacion)
);

-- TABLA RESERVA
create table Reserva(
	idReserva int primary key auto_increment,
    idCliente int not null,
    idEmpleado int not null,
    idHabitacion int not null,
    fechaReserva date not null,
    fechaIngreso date not null,
    fechaSalida date not null,
    estadoReserva varchar(20) not null,
    foreign key (idCliente) references Cliente(idCliente),
    foreign key (idEmpleado) references Empleado(idEmpleado),
    foreign key (idHabitacion) references Habitacion(idHabitacion)
);

-- TABLA PAGO
create table Pago(
	idPago int primary key auto_increment,
    idReserva int not null unique,
    monto decimal(10,2) not null,
    fechaPago date not null,
    metodoPago varchar(50) not null,
    foreign key (idReserva) references Reserva(idReserva)
);
