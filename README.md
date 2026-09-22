# MoodTracker 🌙

App de trackeo de estado de ánimo desarrollada con **Kotlin Compose Multiplatform** y **Supabase**, como proyecto para el desafío técnico de Aranguri Apps.

Permite a los usuarios registrar cómo se sienten día a día, visualizar su evolución emocional a lo largo del tiempo y llevar un historial completo de sus notas de estado de ánimo.

---

## 📱 Capturas de pantalla

<table>
  <tr>
    <td align="center"><b>Inicio de sesión</b><br><img src="https://github.com/user-attachments/assets/ba35a3b6-2747-4ece-92fd-84130e0043c9" width="220"/></td>
    <td align="center"><b>Registro</b><br><img src="https://github.com/user-attachments/assets/8cf04457-38a9-4093-bab8-43c90626bb95" width="220"/></td>
    <td align="center"><b>Home</b><br><img src="https://github.com/user-attachments/assets/891880e7-f8ca-44b2-a064-e662289bea5b" width="220"/></td>
    
  </tr>
  <tr>
   <td align="center"><b>Notas</b><br><img src="https://github.com/user-attachments/assets/81cc9224-81db-4273-91e9-4771a5fe2247" width="220"/></td>
    <td align="center"><b>Perfil</b><br><img src="https://github.com/user-attachments/assets/32c36cf9-f994-4ca7-b109-f9123aef4151" width="220"/></td>
    <td align="center"><b>Editar nota</b><br><img src="https://github.com/user-attachments/assets/a134de33-e049-48fb-90ad-49cff30ec8f0" width="220"/></td>
  </tr>
  <tr>
    <td align="center"><b>Nueva nota</b><br><img src="https://github.com/user-attachments/assets/6214fec7-e0bb-414f-86d2-ebd454358582" width="220"/></td>
      <td align="center"><b>Detalles de la nota</b><br><img src="https://github.com/user-attachments/assets/c97d84b3-d797-4c03-bfc2-a294013d435a"width="220"/></td>
    <td></td>
    <td></td>
  </tr>
</table>



## ✨ Funcionalidades

- **Autenticación** de usuarios (registro y login) vía Supabase Auth
- **Home** con:
  - Saludo dinámico según la hora del día
  - Frase motivacional aleatoria
  - Gráfico de estado de ánimo de los últimos 7 días
  - Gráfico de porcentaje de moods del mes en curso
  - Vista rápida de las últimas notas cargadas
- **Carga de notas de estado de ánimo**: selección de mood (con íconos propios), texto de sentimiento, causa y descripción
- **Historial de notas** con:
  - Filtro por tipo de mood
  - Filtro por fecha
  - Edición y eliminación de notas existentes
- **Detalle de nota**: vista completa de una nota individual
- **Perfil de usuario**:
  - Selección de avatar entre opciones predeterminadas
  - Edición de nombre
  - Cierre de sesión
- **Persistencia de sesión**: el usuario permanece logueado entre aperturas de la app

---

## 🛠️ Stack técnico

- **Kotlin Multiplatform** + **Compose Multiplatform** (Android / iOS)
- **Supabase**
  - Auth (autenticación por email/contraseña)
  - Postgrest (base de datos, con Row Level Security)
- **Koin** — inyección de dependencias
- **Navigation Compose Multiplatform** — navegación con backstack real y argumentos tipados
- **BuildKonfig** — manejo de variables de entorno (credenciales de Supabase) sin hardcodear en el código fuente

### Arquitectura

El proyecto sigue los principios de **Clean Architecture**, separado en tres capas:

```
domain/         → modelos, interfaces de repositorios y casos de uso (use cases)
data/           → implementaciones de repositorios y datasources remotos (Supabase)
presentation/   → ViewModels y pantallas (Composables)
di/             → módulos de Koin (Data, Domain, Presentation)
```

Los **datasources** están definidos como interfaces (`AuthRemoteDataSource`, `NoteRemoteDataSource`, `ProfileRemoteDataSource`) con sus respectivas implementaciones (`...Impl`), desacoplando la lógica de negocio del proveedor de datos concreto (Supabase) y facilitando el testing con dobles de prueba (fakes).

---

## ✅ Testing

El proyecto incluye tests unitarios sobre las tres capas principales, usando fakes hand-written:

- **Auth**: `AuthRepositoryImplTest`, `LoginViewModelTest`, `RegisterViewModelTest`, con `FakeAuthRemoteDataSource` / `FakeAuthRepository`
- **Notes**: `NoteRepositoryImplTest`, `NotesViewModelTest`, con `FakeNoteRemoteDataSource` / `FakeNoteRepository`
- **Profile**: `ProfileRepositoryImplTest`, `ProfileViewModelTest`, con `FakeProfileRemoteDataSource` / `FakeProfileRepository`

---

## 🤖 Herramientas de IA

Durante el desarrollo se utilizó **Claude (Anthropic)** como herramienta de asistencia, principalmente para resolver dudas sobre tecnologías, debuggear errores de compilación y configuración, y agilizar la escritura de código repetitivo.

---

-

## 🚀 Setup del proyecto

> ⚠️ El proyecto completo y funcional se encuentra en la rama **`develop`**. Asegurate de estar parado en esa rama antes de ejecutar la app.

### 1. Clonar el repositorio

```bash
git clone <url-del-repo>
cd MoodTracker
git checkout develop
```

### 2. Configurar Supabase

El archivo `local.properties` con las credenciales de Supabase (`supabase.url` y `supabase.key`) ya está incluido en el repositorio para facilitar la ejecución del proyecto sin pasos adicionales.

> La key incluida es la **Publishable key** (equivalente a la antigua `anon key`), diseñada para ser pública y usada en clientes — la seguridad real de los datos está garantizada por las políticas de Row Level Security (RLS) configuradas en la base de datos, no por el secreto de esta key.

### 3. Base de datos
 
El proyecto utiliza dos tablas principales, ambas con Row Level Security (RLS) habilitado.
 
**Tabla `notes`**
 
| Columna | Tipo | Notas |
|---|---|---|
| `id` | `uuid` | Primary key, default `gen_random_uuid()` |
| `user_id` | `uuid` | FK a `auth.users.id` |
| `mood` | `text` | |
| `feeling_text` | `text` | |
| `cause_text` | `text` | |
| `description` | `varchar` | |
| `created_at` | `timestamptz` | default `now()` |
 
Políticas RLS: `SELECT` / `INSERT` / `UPDATE` / `DELETE` restringidas a `auth.uid() = user_id`.
 
**Tabla `profiles`**
 
| Columna | Tipo | Notas |
|---|---|---|
| `user_id` | `uuid` | Primary key, FK a `auth.users.id` |
| `name` | `text` | |
| `avatar_id` | `text` | |
 
Políticas RLS: `SELECT` / `INSERT` (upsert) / `UPDATE` restringidas a `auth.uid() = user_id`.
 
### 4. Ejecutar

Abrir el proyecto en Android Studio y correr sobre un emulador/dispositivo Android, o abrir el proyecto iOS desde Xcode (requiere macOS).

---

## 🔑 Cuenta de prueba

Para explorar la app con datos ya cargados, sin necesidad de registrarse y cargar notas manualmente:

```
Email: test123@gmail.com
Contraseña: Test123
```
