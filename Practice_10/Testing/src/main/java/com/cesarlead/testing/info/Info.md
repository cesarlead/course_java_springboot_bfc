Test Unitario vs. Integración vs. E2E?

**La Clase a Probar (SUT):**

```java
// UserService.java
// Depende de un REPOSITORIO (externo) y un VALIDADOR (interno)
class UserService {
    private UserRepository userRepository;
    private UserValidator userValidator;

    public UserDto createUser(UserDto userDto) {
        if (!userValidator.isValid(userDto)) {
            throw new ValidationException("Invalid user data");
        }
        User user = mapToEntity(userDto);
        User savedUser = userRepository.save(user);
        return mapToDto(savedUser);
    }
}
```

**El Test Unitario (Nuestro Enfoque):**

```java
// UserServiceTest.java (UNITARIO)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository mockUserRepository; // ¡Un Mock! No hay BD real.

    UserValidator realUserValidator = new UserValidator(); // Dependencia real

    @InjectMocks
    UserService userService;

    @Test
    void testCreateUser() {
        // ... Lógica de Mockito (when/then) ...
        // NO se conecta a ninguna base de datos.
    }
}
```

**El Test de Integración (Capa de Persistencia):**

```java
// UserRepositoryTest.java (INTEGRACIÓN)
@DataJpaTest // Levanta un contexto de Spring solo para JPA
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    TestEntityManager entityManager; // Una BD real (ej. Testcontainers o H2)

    @Autowired
    UserRepository userRepository;

    // EXPECTED_VALUE = "";

    @Test
    void whenFindById_thenReturnsUser() {
        // ... Usa entityManager para insertar un usuario REAL ...
        // ... Llama a userRepository.findById() ...
        // ... Verifica contra la BD real ...
        // assertThat(result).isEqualTo(EXPECTED_VALUE)
    }
}
```

```java
// PriceCalculator.java
// Una clase simple que calcula el precio final con IVA.
public class PriceCalculator {

    private final double IVA_RATE = 0.21; // 21%

    /**
     * Calcula el precio final incluyendo IVA.
     * El requisito de negocio (Contrato) es: precioFinal = precioBase * 1.21
     */
    public double calculateFinalPrice(double priceBase) {
        if (priceBase < 0) {
            throw new IllegalArgumentException("Price base cannot be negative");
        }
        return priceBase * (1 + IVA_RATE);
    }
}
```

**El Test Unitario:**

```java
// PriceCalculatorTest.java

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceCalculatorTest {

    private PriceCalculator calculator = new PriceCalculator();

    @Test
    void givenPositivePrice_whenCalculateFinalPrice_thenReturnsPriceWithIVA() {
        // 1. Arrange (Organizar)
        double priceBase = 100.0;
        double expectedFinalPrice = 121.0; // ¡El "Hardcoding" del Contrato!

        // 2. Act (Actuar)
        double actualFinalPrice = calculator.calculateFinalPrice(priceBase);

        // 3. Assert (Verificar)
        assertThat(actualFinalPrice).isEqualTo(expectedFinalPrice);
    }

    @Test
    void givenNegativePrice_whenCalculateFinalPrice_thenThrowsException() {
        // 1. Arrange (Organizar)
        double invalidPriceBase = -50.0;

        // 2. Act & 3. Assert (Verificar el contrato del "camino triste")
        assertThatThrownBy(() -> {
            calculator.calculateFinalPrice(invalidPriceBase);
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Price base cannot be negative");
    }
}
```

```java
// UserService.java
// (Usaremos este UserService como ejemplo recurrente)

public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    // Inyección de dependencias por constructor (¡Ideal para tests!)
    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public UserDto createUser(String username, String email) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }

        User userToSave = new User(username, email);

        // Lógica de negocio: guardar y notificar
        User savedUser = userRepository.save(userToSave);
        emailService.sendWelcomeEmail(savedUser.getId(), email);

        return new UserDto(savedUser.getId(), savedUser.getUsername());
    }
}

// (Se asume la existencia de User, UserDto, UserRepository, EmailService)
```

**El Test Unitario (Con Anatomía PRO):**

```java
// UserServiceTest.java

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

// 1. Activar la extensión de Mockito para JUnit 5
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    // 2. Crear mocks para TODAS las dependencias
    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private EmailService mockEmailService;

    // 3. Inyectar los mocks en el SUT (System Under Test)
    @InjectMocks
    private UserService userService;

    // (Nota: @InjectMocks usa el constructor de UserService 
    // y le pasa mockUserRepository y mockEmailService)

    // 4. Variables de prueba (opcional, pero útil)
    private User savedUser;

    @BeforeEach
    void setUp() {
        // Este método se ejecuta ANTES de cada @Test
        // Es perfecto para definir mocks que se usan en varios tests

        savedUser = new User(1L, "testUser", "test@mail.com");

        // Stubbing común: cuando se guarde CUALQUIER usuario, devuelve nuestro 'savedUser'
        when(mockUserRepository.save(any(User.class))).thenReturn(savedUser);
    }

    // 5. Nomenclatura BDD + Patrón AAA
    @Test
    void givenValidUser_whenCreateUser_thenReturnsSavedUserDto() {

        // 1. Arrange (Organizar)
        String username = "testUser";
        String email = "test@mail.com";
        // El @BeforeEach ya hizo el 'arrange' del mock de 'save'

        // 2. Act (Actuar)
        UserDto resultDto = userService.createUser(username, email);

        // 3. Assert (Verificar)
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.getId()).isEqualTo(1L);
        assertThat(resultDto.getUsername()).isEqualTo("testUser");
    }

    @Test
    void givenNullUsername_whenCreateUser_thenThrowsIllegalArgumentException() {

        // 1. Arrange (Organizar)
        String nullUsername = null;
        String email = "test@mail.com";

        // 2. Act (Actuar) & 3. Assert (Verificar)
        // (Para excepciones, Act y Assert se combinan)

        assertThatThrownBy(() -> {
            userService.createUser(nullUsername, email);
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Username is required");
    }
}
```

```java
// PriceCalculator.java
public class PriceCalculator {

    private final double IVA_RATE = 0.21; // 21%

    public double calculateFinalPrice(double priceBase) {
        if (priceBase < 0) {
            // Contrato de Error: Precios negativos lanzan esta excepción
            throw new IllegalArgumentException("Price base cannot be negative");
        }
        // Contrato de "Happy Path": precio * 1.21
        return priceBase * (1 + IVA_RATE);
    }

    public boolean isPriceTooHigh(double price) {
        // Contrato Booleano: Más de 1000 es "muy alto"
        return price > 1000;
    }

    public String getStatusMessage(double price) {
        if (price == 0) {
            return null; // Contrato Nulo: Precio 0 no tiene mensaje
        }
        return "Price is: " + price;
    }
}
```

**El Test Unitario (Solo con Aserciones JUnit 5):**

```java
// PriceCalculatorJunitTest.java

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

// Importamos estáticamente TODAS las aserciones de JUnit 5
import static org.junit.jupiter.api.Assertions.*;

class PriceCalculatorJunitTest {

    private PriceCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new PriceCalculator();
    }

    @Test
    @DisplayName("givenPositivePrice_whenCalculateFinalPrice_thenReturnsPriceWithIVA")
    void givenPositivePrice_whenCalculateFinalPrice_thenReturnsPriceWithIVA() {
        // 1. Arrange
        double priceBase = 100.0;
        double expectedFinalPrice = 121.0; // El Contrato "hardcodeado"

        // 2. Act
        double actualFinalPrice = calculator.calculateFinalPrice(priceBase);

        // 3. Assert (Verificando el contrato 'assertEquals')
        assertEquals(expectedFinalPrice, actualFinalPrice);
    }

    @Test
    @DisplayName("givenNegativePrice_whenCalculateFinalPrice_thenThrowsIllegalArgumentException")
    void givenNegativePrice_whenCalculateFinalPrice_thenThrowsIllegalArgumentException() {
        // 1. Arrange
        double invalidPriceBase = -50.0;
        String expectedErrorMessage = "Price base cannot be negative"; // El Contrato del mensaje

        // 2. Act & 3. Assert (Verificando el contrato 'assertThrows')
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    // 2. Act (va dentro de la lambda)
                    calculator.calculateFinalPrice(invalidPriceBase);
                }
        );

        // 3. Assert (Opcional pero recomendado: verificar el mensaje del contrato)
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    @DisplayName("givenHighPrice_whenIsPriceTooHigh_thenReturnsTrue")
    void givenHighPrice_whenIsPriceTooHigh_thenReturnsTrue() {
        // 1. Arrange
        double highPrice = 1001.0;

        // 2. Act
        boolean result = calculator.isPriceTooHigh(highPrice);

        // 3. Assert (Verificando el contrato 'assertTrue')
        assertTrue(result);
    }

    @Test
    @DisplayName("givenZeroPrice_whenGetStatusMessage_thenReturnsNull")
    void givenZeroPrice_whenGetStatusMessage_thenReturnsNull() {
        // 1. Arrange
        double zeroPrice = 0.0;

        // 2. Act
        String message = calculator.getStatusMessage(zeroPrice);

        // 3. Assert (Verificando el contrato 'assertNull')
        assertNull(message);
    }
}
```

```java
// UserProfileService.java
// Servicio que mapea un usuario de dominio a un DTO y realiza cálculos.

// (Clases DTO y Entidad asumidas)
// User: id (Long), username (String), email (String), isActive (boolean)
// UserProfileDto: id (Long), username (String), email (String), status (String)

public class UserProfileService {

    /**
     * Contrato: Mapea una Entidad 'User' a un 'UserProfileDto'.
     * Regla de Negocio 1: User.isActive=true -> Dto.status="ACTIVE"
     * Regla de Negocio 2: User.isActive=false -> Dto.status="INACTIVE"
     */
    public UserProfileDto mapUserToProfileDto(User user) {
        UserProfileDto dto = new UserProfileDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());

        // Lógica de negocio (Contrato)
        if (user.isActive()) {
            dto.setStatus("ACTIVE");
        } else {
            dto.setStatus("INACTIVE");
        }
        return dto;
    }

    /**
     * Contrato: Realiza un cálculo potencialmente intensivo.
     * Requisito No Funcional: Debe completar en menos de 50ms.
     */
    public String performHeavyCalculation() {
        // Simula un trabajo que debe ser rápido
        try {
            Thread.sleep(10); // Simulación de 10ms
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Calculation Complete";
    }
}
```

**El Test Unitario (Con Aserciones Avanzadas):**

```java
// UserProfileServiceJunitTest.java

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.Duration;

// Importaciones estáticas
import static org.junit.jupiter.api.Assertions.*;

class UserProfileServiceJunitTest {

    private UserProfileService service;

    @BeforeEach
    void setUp() {
        service = new UserProfileService();
    }

    @Test
    @DisplayName("givenActiveUser_whenMapUserToProfileDto_thenDtoFieldsAreCorrect")
    void givenActiveUser_whenMapUserToProfileDto_thenDtoFieldsAreCorrect() {
        // 1. Arrange
        User activeUser = new User(1L, "john.doe", "john@mail.com", true);

        // 2. Act
        UserProfileDto dto = service.mapUserToProfileDto(activeUser);

        // 3. Assert (Verificando el contrato COMPLETO del DTO)
        assertAll(
                "Verificar todos los campos del UserProfileDto", // Mensaje de cabecera
                () -> assertEquals(1L, dto.getId()),
                () -> assertEquals("john.doe", dto.getUsername()),
                () -> assertEquals("john@mail.com", dto.getEmail()),
                () -> assertEquals("ACTIVE", dto.getStatus()) // El Contrato de negocio
        );
    }

    @Test
    @DisplayName("givenInactiveUser_whenMapUserToProfileDto_thenStatusIsInactive")
    void givenInactiveUser_whenMapUserToProfileDto_thenStatusIsInactive() {
        // 1. Arrange
        User inactiveUser = new User(2L, "jane.doe", "jane@mail.com", false);

        // 2. Act
        UserProfileDto dto = service.mapUserToProfileDto(inactiveUser);

        // 3. Assert (Solo verificamos el contrato que cambia)
        assertEquals("INACTIVE", dto.getStatus());
    }

    @Test
    @DisplayName("whenPerformHeavyCalculation_thenCompletesWithin50Milliseconds")
    void whenPerformHeavyCalculation_thenCompletesWithin50Milliseconds() {
        // 1. Arrange
        Duration timeout = Duration.ofMillis(50); // El Contrato de Tiempo

        // 2. Act & 3. Assert
        assertTimeout(
                timeout,
                () -> {
                    // 2. Act (va dentro de la lambda)
                    service.performHeavyCalculation();
                }
        );

        // Si el método tarda 51ms o más, el test falla.
    }
}
```

```java
// OrderService.java
// (Clases DTO y Entidad asumidas)
// Order (Entity): id, customerName, OrderStatus status, List<OrderItem> items
// OrderItem (Entity): description, quantity
// OrderDto (DTO): id, customerName, OrderStatus status, List<OrderItemDto> items
// OrderItemDto (DTO): description, quantity

public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Contrato: Devuelve un DTO de la orden con sus items.
     * Lanza OrderNotFoundException si no existe.
     */
    public OrderDto getOrderDetails(long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderId));

        // Lógica de mapeo
        return mapToDto(order);
    }

    /**
     * Contrato: Devuelve solo las órdenes que coinciden con el estado.
     */
    public List<OrderDto> findOrdersByStatus(OrderStatus status) {
        return orderRepository.findAllByStatus(status).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // (mapToDto es un helper privado)
    private OrderDto mapToDto(Order order) { /* ... */ }
}
```

**El Test Unitario (Con Poder de AssertJ):**

```java
// OrderServiceAssertJTest.java

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

// 1. LA IMPORTACIÓN MÁGICA
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceAssertJTest {

    @Mock
    private OrderRepository mockOrderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void givenValidOrderId_whenGetOrderDetails_thenReturnsCorrectDto() {
        // 1. Arrange
        // (Configuración más compleja para un test robusto)
        OrderItem item1 = new OrderItem("Laptop", 1);
        OrderItem item2 = new OrderItem("Mouse", 2);
        Order order = new Order(1L, "Cesar Lead", OrderStatus.SHIPPED, List.of(item1, item2));

        // (Asumimos que mapToDto funciona, creamos el CONTRATO esperado)
        OrderItemDto itemDto1 = new OrderItemDto("Laptop", 1);
        OrderItemDto itemDto2 = new OrderItemDto("Mouse", 2);
        OrderDto expectedDto = new OrderDto(1L, "Cesar Lead", OrderStatus.SHIPPED, List.of(itemDto1, itemDto2));

        when(mockOrderRepository.findById(1L)).thenReturn(Optional.of(order));

        // 2. Act
        OrderDto actualDto = orderService.getOrderDetails(1L);

        // 3. Assert (El poder de AssertJ)

        // Opción 1: Aserción Recursiva (La mejor para DTOs)
        // "Verifico que mi DTO 'actual' es idéntico en todos sus campos al DTO 'esperado'"
        assertThat(actualDto)
                .usingRecursiveComparison()
                .isEqualTo(expectedDto);
    }

    @Test
    void givenValidOrderId_whenGetOrderDetails_thenReturnsCorrectDto_Manual() {
        // 1. Arrange
        OrderItem item1 = new OrderItem("Laptop", 1);
        OrderItem item2 = new OrderItem("Mouse", 2);
        Order order = new Order(1L, "Cesar Lead", OrderStatus.SHIPPED, List.of(item1, item2));
        when(mockOrderRepository.findById(1L)).thenReturn(Optional.of(order));

        // 2. Act
        OrderDto actualDto = orderService.getOrderDetails(1L);

        // 3. Assert (Opción 2: Manualmente, para mostrar el poder de las colecciones)
        assertThat(actualDto).isNotNull();
        assertThat(actualDto.getCustomerName()).isEqualTo("Cesar Lead");
        assertThat(actualDto.getStatus()).isEqualTo(OrderStatus.SHIPPED);

        // Aserciones de Colección:
        assertThat(actualDto.getItems())
                .hasSize(2)
                .extracting("description") // Extrae la propiedad 'description' de cada item
                .containsExactlyInAnyOrder("Mouse", "Laptop"); // Verifica los nombres

        assertThat(actualDto.getItems())
                .extracting("quantity") // Extrae la propiedad 'quantity'
                .containsExactlyInAnyOrder(1, 2); // Verifica las cantidades
    }

    @Test
    void givenOrdersInDb_whenFindOrdersByStatus_thenReturnsOnlyActiveOrders() {
        // 1. Arrange
        Order order1 = new Order(1L, "User 1", OrderStatus.ACTIVE, List.of());
        Order order2 = new Order(2L, "User 2", OrderStatus.PENDING, List.of());
        Order order3 = new Order(3L, "User 3", OrderStatus.ACTIVE, List.of());

        when(mockOrderRepository.findAllByStatus(OrderStatus.ACTIVE))
                .thenReturn(List.of(order1, order3)); // El mock ya filtra

        // 2. Act
        List<OrderDto> results = orderService.findOrdersByStatus(OrderStatus.ACTIVE);

        // 3. Assert (Verificando el contrato de la lista)
        assertThat(results)
                .hasSize(2)
                .extracting("id") // "De la lista de DTOs, saca todos los IDs..."
                .contains(1L, 3L); // "...y verifica que estos IDs están presentes."

        assertThat(results)
                .extracting("status") // "Saca todos los status..."
                .containsOnly(OrderStatus.ACTIVE); // "...y verifica que SÓLO hay 'ACTIVE'."
    }

    @Test
    void givenInvalidOrderId_whenGetOrderDetails_thenThrowsOrderNotFoundException() {
        // 1. Arrange
        long invalidId = 99L;
        when(mockOrderRepository.findById(invalidId)).thenReturn(Optional.empty());

        String expectedMessage = "Order not found: " + invalidId;

        // 2. Act & 3. Assert (Verificando el contrato de excepción)
        assertThatThrownBy(() -> {
            orderService.getOrderDetails(invalidId); // 2. Act
        })
                .isInstanceOf(OrderNotFoundException.class) // 3. Assert (Tipo)
                .hasMessage(expectedMessage); // 3. Assert (Mensaje)
    }
}
```

```java
// UserReportService.java
// Este servicio tiene métodos que devuelven los tres escenarios
public class UserReportService {

    private final UserRepository userRepository;

    public UserReportService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Escenario 1: Booleano
     * Contrato: Es elegible si es un usuario "premium".
     */
    public boolean isUserEligibleForReport(long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return user != null && user.getType() == UserType.PREMIUM;
    }

    /**
     * Escenario 2: DTO
     * Contrato: Devuelve un DTO con los detalles del usuario.
     */
    public UserReportDto generateReport(long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        // Lógica de mapeo
        return new UserReportDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getType()
        );
    }

    /**
     * Escenario 3: Lista
     * Contrato: Devuelve solo los usuarios ACTIVOS.
     */
    public List<UserReportDto> getActiveUserReports() {
        return userRepository.findAllByStatus(UserStatus.ACTIVE)
                .stream()
                .map(user -> new UserReportDto(user.getId(), user.getUsername(), user.getEmail(), user.getType()))
                .collect(Collectors.toList());
    }
}
```

**El Test Unitario (Taller Comparativo):**

```java
// UserReportServiceWorkshopTest.java

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

// 1. Importar SOLO AssertJ
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserReportServiceWorkshopTest {

    @Mock
    private UserRepository mockUserRepository;

    @InjectMocks
    private UserReportService service;

    // --- Escenario 1: Test de Booleano ---
    @Test
    void givenPremiumUser_whenIsUserEligible_thenReturnsTrue() {
        // Arrange
        User premiumUser = new User(1L, "premium", "a@b.com", UserStatus.ACTIVE, UserType.PREMIUM);
        when(mockUserRepository.findById(1L)).thenReturn(Optional.of(premiumUser));

        // Act
        boolean isEligible = service.isUserEligibleForReport(1L);

        // Assert (El camino AssertJ. Simple y consistente)
        assertThat(isEligible).isTrue();
    }

    // --- Escenario 2: Test de DTO ---
    @Test
    void givenValidUser_whenGenerateReport_thenReturnsCorrectDto() {
        // Arrange
        User user = new User(1L, "testUser", "test@mail.com", UserStatus.ACTIVE, UserType.REGULAR);
        when(mockUserRepository.findById(1L)).thenReturn(Optional.of(user));

        // El CONTRATO "hardcodeado"
        UserReportDto expectedReport = new UserReportDto(1L, "testUser", "test@mail.com", UserType.REGULAR);

        // Act
        UserReportDto actualReport = service.generateReport(1L);

        // Assert (El camino profesional: AssertJ)
        assertThat(actualReport)
                .usingRecursiveComparison()
                .isEqualTo(expectedReport);
        
        /*
        // Assert (El camino "Aceptable" pero verboso: JUnit 5 assertAll)
        assertAll(
            "Report DTO",
            () -> assertEquals(expectedReport.getId(), actualReport.getId()),
            () -> assertEquals(expectedReport.getUsername(), actualReport.getUsername()),
            () -> assertEquals(expectedReport.getEmail(), actualReport.getEmail()),
            () -> assertEquals(expectedReport.getType(), actualReport.getType())
        );
        */
    }

    // --- Escenario 3: Test de Lista ---
    @Test
    void whenGetActiveUserReports_thenReturnsOnlyActiveUsers() {
        // Arrange
        User user1 = new User(1L, "User 1", "u1@mail.com", UserStatus.ACTIVE, UserType.REGULAR);
        User user2 = new User(2L, "User 2", "u2@mail.com", UserStatus.INACTIVE, UserType.REGULAR);
        User user3 = new User(3L, "User 3", "u3@mail.com", UserStatus.ACTIVE, UserType.PREMIUM);

        List<User> mockReturn = List.of(user1, user3);
        when(mockUserRepository.findAllByStatus(UserStatus.ACTIVE)).thenReturn(mockReturn);

        // Act
        List<UserReportDto> activeUsers = service.getActiveUserReports();

        // Assert (El camino profesional: AssertJ)
        // Verificamos el contrato del filtro
        assertThat(activeUsers)
                .hasSize(2) // 1. Verifica el tamaño
                .extracting("username") // 2. Extrae las propiedades
                .containsExactlyInAnyOrder("User 1", "User 3"); // 3. Verifica el contenido
            
        /*
        // Assert (El MAL camino: Lógica en el test. ¡PROHIBIDO!)
        assertEquals(2, activeUsers.size());
        for (UserReportDto dto : activeUsers) {
            // ¡Esto es código, no un test!
            if (dto.getUsername().equals("User 2")) {
                fail("Found inactive user");
            }
        }
        */
    }
}
```

```java
// FormattingService.java
// Una dependencia que podríamos querer espiar (Spy)
public class FormattingService {

    // Método que queremos sobreescribir
    public String formatTitle(String username) {
        return "Official Report for: " + username.toUpperCase();
    }

    // Método que queremos que ejecute su código REAL
    public String formatSubtitle(String date) {
        return "Generated on: " + date;
    }
}

// ReportGeneratorService.java (El SUT)
// Depende de un Repositorio y del FormattingService
public class ReportGeneratorService {
    private final UserRepository userRepository;
    private final FormattingService formattingService;

    public ReportGeneratorService(UserRepository userRepository, FormattingService formattingService) {
        this.userRepository = userRepository;
        this.formattingService = formattingService;
    }

    public Report generateReport(long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        // Llama al primer método de la dependencia
        String title = formattingService.formatTitle(user.getUsername());

        // Llama al segundo método de la dependencia
        String subtitle = formattingService.formatSubtitle("today");

        return new Report(title, subtitle);
    }
}
```

**El Test Unitario (El Trío en Acción):**

```java
// ReportGeneratorServiceTrioTest.java

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportGeneratorServiceTrioTest {

    // 1. El SIMULADOR (Cáscara vacía para la BD)
    @Mock
    private UserRepository mockUserRepository;

    // 2. El AGENTE DOBLE (Objeto real, envuelto)
    // ¡Debemos instanciarlo nosotros!
    @Spy
    private FormattingService spyFormattingService = new FormattingService();

    // 3. El ENSAMBLADOR (El SUT real)
    // Inyectará el @Mock y el @Spy en el constructor
    @InjectMocks
    private ReportGeneratorService reportService;

    @Test
    void givenUser_whenGenerateReport_thenUsesRealAndStubbedSpyMethods() {

        // 1. Arrange
        User user = new User(1L, "testUser", ...);

        // Programamos el @Mock (Aislamiento Total)
        when(mockUserRepository.findById(1L)).thenReturn(Optional.of(user));

        // Programamos el @Spy:
        // "Quiero sobreescribir 'formatTitle'..."
        doReturn("Mocked Title").when(spyFormattingService).formatTitle("testUser");
        // "...pero dejaré que 'formatSubtitle' ejecute su código real."

        // 2. Act
        Report report = reportService.generateReport(1L); // El SUT llama a sus dependencias

        // 3. Assert
        assertThat(report).isNotNull();

        // Verificamos el método STUBBED (sobreescrito) del Spy
        assertThat(report.getTitle()).isEqualTo("Mocked Title");

        // Verificamos el método REAL del Spy (¡Peligro!)
        assertThat(report.getSubtitle()).isEqualTo("Generated on: today");
    }
}
```

```java
// UserService.java
// (Usamos la versión de la Lección 1.3)
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public UserDto createUser(String username, String email) {
        if (userRepository.existsByEmail(email)) {
            // Contrato de Error de Negocio
            throw new EmailAlreadyExistsException("Email already in use: " + email);
        }

        User userToSave = new User(username, email);

        User savedUser = userRepository.save(userToSave);
        emailService.sendWelcomeEmail(savedUser.getId(), email);

        return new UserDto(savedUser.getId(), savedUser.getUsername());
    }
}
```

**El Test Unitario (Con Stubbing BDD):**

```java
// UserServiceStubbingTest.java

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// 1. Importar BDDMockito (además de AssertJ)
import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceStubbingTest {

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private EmailService mockEmailService;

    @InjectMocks
    private UserService userService;

    @Test
    void givenNewEmail_whenCreateUser_thenReturnsSavedUserDto() {

        // 1. Arrange (¡Aquí está el Stubbing!)

        // 1a. Definición del Contexto ("Hardcoding" del escenario)
        String username = "testUser";
        String email = "new@mail.com";

        User userToSave = new User(username, email); // Esto lo usará el SUT
        User savedUser = new User(1L, username, email); // Esto lo devolverá el mock

        // 1b. Programación de Mocks (Stubbing BDD)

        // "Dado que (given) el repositorio... 
        //  ...cuando se llame a 'existsByEmail' con este email...
        //  ...devolverá (willReturn) 'false' (email no existe)."
        given(mockUserRepository.existsByEmail(email)).willReturn(false);

        // "Dado que (given) el repositorio...
        //  ...cuando se llame a 'save' con CUALQUIER (any) objeto User...
        //  ...devolverá (willReturn) nuestro 'savedUser' predefinido."
        given(mockUserRepository.save(any(User.class))).willReturn(savedUser);

        // 2. Act (Ejecutar el SUT)
        UserDto resultDto = userService.createUser(username, email);

        // 3. Assert (Verificar el contrato)
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.getId()).isEqualTo(1L);
        assertThat(resultDto.getUsername()).isEqualTo(username);
    }

    @Test
    void givenExistingEmail_whenCreateUser_thenThrowsEmailAlreadyExistsException() {

        // 1. Arrange (Programando el "camino triste")
        String email = "existing@mail.com";

        // "Dado que (given) el repositorio...
        //  ...cuando se llame a 'existsByEmail' con este email...
        //  ...devolverá (willReturn) 'true' (email SÍ existe)."
        given(mockUserRepository.existsByEmail(email)).willReturn(true);

        // ¡No necesitamos programar el mock de 'save()'! ¿Por qué?
        // Porque el test DEBE fallar y salir ANTES de llamar a 'save()'.

        // 2. Act & 3. Assert (Verificar el contrato de error)
        assertThatThrownBy(() -> {
            userService.createUser("anyUser", email); // 2. Act
        })
                .isInstanceOf(EmailAlreadyExistsException.class) // 3. Assert
                .hasMessageContaining(email);
    }
}
```

```java
// UserService.java
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, EmailService emailService) { /* ... */ }

    public UserDto createUser(String username, String email) {
        if (userRepository.existsByEmail(email)) {
            // Camino triste
            throw new EmailAlreadyExistsException("Email already in use: " + email);
        }

        // Camino feliz
        User userToSave = new User(username, email);
        User savedUser = userRepository.save(userToSave);

        // ¡EL EFECTO SECUNDARIO QUE QUEREMOS PROBAR!
        emailService.sendWelcomeEmail(savedUser.getId(), email);

        return new UserDto(savedUser.getId(), savedUser.getUsername());
    }
}
```

**El Test Unitario (Con Verificación de Comportamiento):**

```java
// UserServiceVerifyTest.java

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// 1. Importaciones estáticas
import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify; // Para `verify`
import static org.mockito.Mockito.never; // Para `never`

@ExtendWith(MockitoExtension.class)
class UserServiceVerifyTest {

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private EmailService mockEmailService; // El mock que vamos a VERIFICAR

    @InjectMocks
    private UserService userService;

    @Test
    void givenNewEmail_whenCreateUser_thenSendsWelcomeEmail() {

        // 1. Arrange (Stubbing)
        String username = "testUser";
        String email = "new@mail.com";
        User savedUser = new User(1L, username, email);

        given(mockUserRepository.existsByEmail(email)).willReturn(false);
        given(mockUserRepository.save(any(User.class))).willReturn(savedUser);

        // 2. Act
        UserDto resultDto = userService.createUser(username, email);

        // 3. Assert

        // 3a. Assert de Estado (Verificar el resultado)
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.getId()).isEqualTo(1L);

        // 3b. Assert de Comportamiento (Verificar el "contrato de email")
        // "Verifico que el mockEmailService fue llamado 1 vez,
        //  en su método 'sendWelcomeEmail',
        //  exactamente con el ID 1L y el email 'new@mail.com'"
        verify(mockEmailService, times(1)).sendWelcomeEmail(1L, email);
    }

    @Test
    void givenExistingEmail_whenCreateUser_thenNeverSendsEmail() {

        // 1. Arrange (Stubbing del "camino triste")
        String email = "existing@mail.com";
        given(mockUserRepository.existsByEmail(email)).willReturn(true);

        // 2. Act & 3a. Assert de Estado (Verificar la excepción)
        assertThatThrownBy(() -> {
            userService.createUser("anyUser", email); // 2. Act
        })
                .isInstanceOf(EmailAlreadyExistsException.class);

        // 3b. Assert de Comportamiento (Verificar el "contrato de NO email")
        // "Verifico que el mockEmailService NUNCA
        //  fue llamado en su método 'sendWelcomeEmail' con NINGÚN (any) argumento"
        verify(mockEmailService, never()).sendWelcomeEmail(anyLong(), anyString());

        // Opcional, pero más estricto:
        verifyNoMoreInteractions(mockEmailService);

        // También verificamos que NUNCA se guardó
        verify(mockUserRepository, never()).save(any(User.class));
    }
}
```

```java
// PasswordEncoder (Interfaz de Spring Security)
public interface PasswordEncoder {
    String encode(CharSequence rawPassword);

    boolean matches(CharSequence rawPassword, String encodedPassword);
}

// UserService.java (Modificado)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Contrato de Negocio:
     * 1. Debe encriptar la contraseña antes de guardarla.
     * 2. Debe poner el estado del usuario en PENDING_ACTIVATION.
     */
    public UserDto createUser(String username, String rawPassword) {
        User userToSave = new User();
        userToSave.setUsername(username);

        // ¡Lógica de Negocio Interna!
        String encodedPassword = passwordEncoder.encode(rawPassword);
        userToSave.setPassword(encodedPassword);
        userToSave.setStatus(UserStatus.PENDING_ACTIVATION);

        User savedUser = userRepository.save(userToSave); // ¡Queremos capturar 'userToSave'!

        return new UserDto(savedUser.getId(), savedUser.getUsername());
    }
}
```

**El Test Unitario (Con `ArgumentCaptor`):**

```java
// UserServiceCaptorTest.java

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor; // 1. Importación
import org.mockito.Captor; // 2. Importación
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceCaptorTest {

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @InjectMocks
    private UserService userService;

    // 3. Declarar el Captor para la clase 'User'
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Test
    void whenCreateUser_thenSavesUserWithEncodedPasswordAndPendingStatus() {

        // 1. Arrange
        String username = "testUser";
        String rawPassword = "password123"; // Contraseña en texto plano
        String encodedPassword = "ksjdfh8723yhkjsdfh"; // Contraseña "hardcodeada"

        User savedUser = new User(1L, username, encodedPassword, UserStatus.PENDING_ACTIVATION);

        // 1a. Stubbing (Programación de Mocks)

        // "Dado que el 'encoder' es llamado con 'password123', devolverá 'ksjdfh...'"
        given(mockPasswordEncoder.encode(rawPassword)).willReturn(encodedPassword);

        // "Dado que el 'repo' es llamado con CUALQUIER 'User', devolverá 'savedUser'"
        given(mockUserRepository.save(any(User.class))).willReturn(savedUser);

        // 2. Act
        UserDto resultDto = userService.createUser(username, rawPassword);

        // 3. Assert

        // 3a. Assert de Estado (Verificar el DTO de salida)
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.getId()).isEqualTo(1L);

        // 3b. Assert de Comportamiento (¡La Captura!)

        // "Verifico que 'save' fue llamado 1 vez, y CAPTURO el argumento"
        verify(mockUserRepository, times(1)).save(userArgumentCaptor.capture());

        // "Saco el objeto 'User' de la trampa"
        User capturedUser = userArgumentCaptor.getValue();

        // 3c. Assert sobre el objeto capturado (¡El Contrato de Negocio Interno!)
        assertThat(capturedUser.getUsername()).isEqualTo(username);
        assertThat(capturedUser.getStatus()).isEqualTo(UserStatus.PENDING_ACTIVATION);
        assertThat(capturedUser.getPassword()).isEqualTo(encodedPassword);
    }
}
```



