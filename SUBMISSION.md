## Overview  

I began by structuring the **Spring Boot application** to ensure a scalable and maintainable codebase. The focus was on implementing **product listing, filtering, authentication, and database seeding** to create a functional e-commerce backend.  

The **filtering system** was designed to support **search queries, category, brand, color, shoe size, and price range**. This was implemented using **Spring Data JPA Specifications**, allowing flexible and efficient query construction.  

For authentication, I integrated **JWT-based authentication** with **role-based access control (RBAC)** to protect certain endpoints, such as the **database seeding feature**, which is restricted to **admin users** only.  

Additionally, I implemented **database seeding**, ensuring that if no admin exists, one is automatically created at application startup. The seeding feature also allows admins to populate the database with sample products dynamically.  

## CHANGELOG  

### Product Management  

#### Backend  

- **Implemented dynamic product filtering and search**  
  - Users can search by **full or partial product name and brand** (case-insensitive).  
  - Added filtering for **categories, brands, colors, shoe sizes, and price range**.  
  - Optimized query execution using **Spring Data JPA Specifications**.  
- **Implemented sorting and pagination**  
  - Products can be sorted by **price and name**.  
  - Pagination ensures that only the necessary data is retrieved.  
- **Ensured proper validation on product creation and updates**  
  - Prevented **empty names, negative prices, and missing categories/brands**.  
  - Automatically fetches product **image URLs from Unsplash** based on color.  

### Authentication & Security  

- **JWT-based authentication system**  
  - Users can **register and log in** securely.  
  - Implemented **role-based access control (RBAC)** to restrict admin functionalities.  
- **Protected sensitive endpoints**  
  - **`/api/seed`** is accessible only to **admin users**.  
  - **`/api/auth/me`** retrieves logged-in user details.  

### Database Seeding  

- **Admin user is created on startup if none exists**.  
- **Admins can populate the database dynamically** via `/api/seed`.  
- **Preloaded database with 20+ diverse products**, categorized as **Sneakers, Loafers, Oxfords, Boat Shoes, Running Shoes, and more**.  
- **Restricted database seeding to prevent duplicate entries**.  

Took approximately **4-5 hours** to complete.