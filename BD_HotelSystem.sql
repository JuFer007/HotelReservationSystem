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

-- LLENADO TEMPORAL
-- 1) TipoHabitacion (5 inserts)
INSERT INTO TipoHabitacion (descripcion, precio) VALUES
('Individual - cama sencilla, baño privado', 50.00),
('Doble - 2 camas, baño privado', 75.50),
('Matrimonial - cama matrimonial, vista', 85.00),
('Suite Ejecutiva - cama king, sala, minibar', 150.00),
('Familiar - 2 ambientes, hasta 4 personas', 120.00);

-- 2) Empleado (5 inserts)
INSERT INTO Empleado (nombre, apellidoPaterno, apellidoMaterno, dni, telefono) VALUES
('Junior', 'Zuameta', 'Golac', '71374454', '+51 987654321'),
('Marcelo', 'Alarcon', 'Manay', '71433244', '+51 987654322'),
('Cristian', 'Huaman', 'Cruz', '73381544', '+51 987654323'),
('Ana', 'Gonzales', 'Flores', '15975348', '+51 987654324'),
('Diego', 'Vargas', 'Ruiz', '25814736', '+51 987654325');

-- 3) Usuario (5 inserts)  -> idEmpleado debe existir en Empleado
INSERT INTO Usuario (idEmpleado, nombreUsuario, contraseña) VALUES
(1, 'jufer07', 'jubia07'),
(2, 'polsent', 'Mar2025'),
(3, 'chriso', 'chriso19'),
(4, 'agonzales', 'Ana#2025'),
(5, 'dvargas', 'Diego$2025');

-- 4) Recepcionista (5 inserts) -> idEmpleado referencia Empleado
INSERT INTO Recepcionista (idEmpleado, turnoTrabajo) VALUES
(1, 'Mañana'),
(2, 'Tarde'),
(3, 'Noche'),
(4, 'Mañana'),
(5, 'Tarde');

-- 5) Administrador (5 inserts) -> idEmpleado referencia Empleado
INSERT INTO Administrador (idEmpleado, correoElectronico) VALUES
(1, 'cparedes@hotel.example.com'),
(2, 'msanchez@hotel.example.com'),
(3, 'jtorres@hotel.example.com'),
(4, 'agonzales@hotel.example.com'),
(5, 'dvargas@hotel.example.com');

-- 6) Cliente (5 inserts)
INSERT INTO Cliente (nombre, apellidoPaterno, apellidoMaterno, dni, telefono, email) VALUES
('Luis', 'Chafloque', 'Avellaneda', '11122233', '+51 945111222', 'luis.huaman@example.com'),
('Maria', 'Lopez', 'Sanchez', '22233344', '+51 945222333', 'maria.lopez@example.com'),
('Pedro', 'Quispe', 'Ayala', '33344455', '+51 945333444', 'pedro.quispe@example.com'),
('Sofia', 'Reyes', 'Gomez', '44455566', '+51 945444555', 'sofia.reyes@example.com'),
('Andres', 'Delgado', 'Torres', '55566677', '+51 945555666', 'andres.delgado@example.com');

-- 7) Habitacion (5 inserts) -> idTipoHabitacion debe existir en TipoHabitacion
INSERT INTO Habitacion (idTipoHabitacion, numeroHabitacion, estado) VALUES
(1, '101', 'Disponible'),
(2, '102', 'Ocupada'),
(3, '103', 'Mantenimiento'),
(4, '104', 'Disponible'),
(5, '105', 'Reservada');

-- 8) Reserva (5 inserts)
-- fechaReserva: fecha en que se hace la reserva
-- fechaIngreso / fechaSalida: periodo reservado
INSERT INTO Reserva (idCliente, idEmpleado, idHabitacion, fechaReserva, fechaIngreso, fechaSalida, estadoReserva) VALUES
(1, 1, 2, '2025-11-01', '2025-11-10', '2025-11-12', 'Finalizada'),
(2, 2, 1, '2025-11-05', '2025-11-20', '2025-11-22', 'En proceso'),
(3, 3, 5, '2025-11-10', '2025-12-01', '2025-12-05', 'Confirmada'),
(4, 4, 4, '2025-11-12', '2025-11-25', '2025-11-27', 'Cancelada'),
(5, 5, 3, '2025-11-15', '2025-12-15', '2025-12-20', 'Confirmada');

-- 9) Pago (5 inserts) -> idReserva referencia Reserva (UNIQUE por diseño)
INSERT INTO Pago (idReserva, monto, fechaPago, metodoPago) VALUES
(1, 151.00, '2025-11-12', 'Tarjeta de crédito'),
(2, 50.00, '2025-11-20', 'Efectivo'),
(3, 120.00, '2025-11-30', 'Yape'),
(4, 0.00, '2025-11-13', 'No aplica'), -- ejemplo para reserva cancelada
(5, 600.00, '2025-11-16', 'Transferencia bancaria');
