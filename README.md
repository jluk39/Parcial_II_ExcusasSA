# Excusas S.A. - Sistema de Gestión de Excusas para Empleados

Este proyecto es una implementación en Java y Spring Boot de un sistema para la gestión de excusas presentadas por empleados. Aplica múltiples patrones de diseño orientado a objetos (OOP) y principios SOLID, y sigue una arquitectura limpia y modular.

---

## 📦 Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/excusas/
│   │   ├── ExcusasSaApplication.java        # Clase principal de Spring Boot
│   │   ├── controller/
│   │   │   └──ExcusaController.java
│   │   ├── dto/
│   │   │   ├── ExcusaRequestDTO.java
│   │   │   └── ExcusaResponseDTO.java
│   │   ├── excepciones/
│   │   │   └──ExcusaNoManejadaException.java
│   │   ├── model/
│   │   │   ├── empleado/
│   │   │   │   ├── encargado/                
│   │   │   │   │   ├── EncargadoBase.java
│   │   │   │   │   ├── CEO.java
│   │   │   │   │   ├── Recepcionista.java
│   │   │   │   │   ├── SupervisorArea.java
│   │   │   │   │   ├── GerenteRRHH.java
│   │   │   │   │   ├── Rechazador.java
│   │   │   │   │   └── IEncargado.java
│   │   │   │   └── Empleado.java
│   │   │   └── estrategia/
│   │   │       ├── IModoResolucion.java
│   │   │       ├── Normal.java
│   │   │       ├── Vago.java
│   │   │       └── Productivo.java
│   │   ├── excusa/
│   │   │   ├── Excusa.java
│   │   │   ├── IExcusa.java
│   │   │   ├── Prontuario.java
│   │   │   └── motivo/
│   │   │       ├── IMotivoExcusa.java
│   │   │       ├── MotivoExcusa.java
│   │   │       ├── Trivial.java
│   │   │       ├── Moderada.java
│   │   │       ├── Compleja.java
│   │   │       ├── Inverosimil.java
│   │   │       ├── CorteLuz.java
│   │   │       └── CuidadoFamiliar.java
│   │   ├── observer/
│   │   │   ├── AdministradorProntuarios.java
│   │   │   ├── IObservable.java
│   │   │   └── IObserver.java
│   │   ├── procesamiento/
│   │   │   ├── encargado/
│   │   │   └── modo/
│   │   ├── repository/
│   │   │   └── ExcusaRepository.java
│   │   │
│   │   ├── service/
│   │   │   ├── ExcusaService.java 
│   │   │   ├── IEmailSender.java
│   │   │   ├── LineaDeEncargados.java
│   │   │   └── EmailSenderImpl.java
├── test/
│   └── java/com/excusas/
│       ├── EmpleadoExcusasTest.java
│       ├── ExcusaincorrectaTest.java
│       ├── ModoResolucionTest.java
│       ├── RecepcionistaTest.java
│       ├── excusas/
│       │   └── ExcusasSaApplicationTests.java
│       └── integration/
│           └── ExcusaIntegrationTests.java
└── resources/
    └── application.properties
```

---

## 🧱 Patrones de Diseño Aplicados

### ✅ Chain of Responsibility
- Permite encadenar los diferentes tipos de encargados (`Recepcionista`, `SupervisorArea`, `GerenteRRHH`, `CEO`) para que cada uno decida si puede procesar una excusa o delegarla.
- Se usa `EncargadoBase` como clase abstracta que implementa `IEncargado`.
- Clase `Rechazador` actúa como final de la cadena.

### ✅ Strategy
- Define estrategias de resolución: `Normal`, `Vago`, `Productivo`, implementando la interfaz `IModoResolucion`.
- Cada encargado tiene una estrategia que define cómo maneja una excusa.

### ✅ Observer + Singleton
- El `AdministradorProntuarios` implementa `IObservable` como singleton y notifica a observadores (`CEO`).
- Permite registrar todas las excusas procesadas para auditoría.

### ✅ Template Method
- `EncargadoBase` define el esqueleto del procesamiento y delega en métodos de plantilla.

---

## 🧠 Lógica del Sistema

### Flujo principal de excusa:

1. Un `Empleado` crea una `Excusa` con un `IMotivoExcusa`.
2. Llama al método `generarExcusa(...)` → devuelve una instancia de `Excusa`.
3. El controller (`ExcusasController`) recibe la excusa y la pasa al primer `Encargado` (ej: `Recepcionista`).
4. La excusa atraviesa la cadena de responsabilidad.
5. Cada encargado aplica su `IModoResolucion` (Normal, Vago, Productivo).
6. Si se resuelve, se notifica a `AdministradorProntuarios`, quien guarda la excusa y notifica al `CEO`.
7. Si nadie puede procesarla, la excusa se rechaza.

---

## 🔬 Testing

- Se utilizan tests unitarios con JUnit 5 para:
    - Empleados que generan excusas
    - Encargados que procesan excusas según el tipo
    - Modos de resolución
    - Casos límite como excusas inverosímiles

- Se incluyen tests de integración en `/test/com/excusas/ExcusaIntegrationTests.java` que prueban la API REST y su respuesta esperada.

---

## 🌐 API REST (en desarrollo)

La API será expuesta vía Spring Boot:

### Endpoints esperados:

- `POST /excusas`: registrar nueva excusa
- `GET /prontuarios`: listar todas las excusas registradas
- `GET /encargados`: obtener estado actual de la cadena
- `POST /encargados/cambiarModo`: cambiar modo de resolución de un encargado

La persistencia es en memoria utilizando `List<>`, y luego se adaptará a base de datos.

---

## 🎯 Objetivos

- Aplicar diseño orientado a objetos con patrones y principios SOLID.
- Simular un sistema empresarial de manejo de excusas con control jerárquico.
- Testear el comportamiento con unit tests.
- Exponer la lógica vía API REST para integrar frontend u otros sistemas.

---

## 🏁 Tags importantes

- `initial`: estructura del proyecto + modelos del parcial I
- `controller`: incluye capa REST y pruebas de integración
- `final`: versión final con almacenamiento persistente y controladores funcionales

---

## 👨‍💻 Autor

- Alumnos:
  - Andrés Berillo
  - Juan Manuel Lukaszewicz
- Colaborador: @underabloodysky (profesor)
