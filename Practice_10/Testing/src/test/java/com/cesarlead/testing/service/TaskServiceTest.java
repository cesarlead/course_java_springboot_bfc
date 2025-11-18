package com.cesarlead.testing.service;

import com.cesarlead.testing.dto.TaskDto;
import com.cesarlead.testing.exception.TaskNotFoundException;
import com.cesarlead.testing.model.Task;
import com.cesarlead.testing.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * El "Porqué":
 *
 * @ExtendWith(MockitoExtension.class) es el "activador".
 * Le dice a JUnit que "active la magia de Mockito".
 * Específicamente, inicializará cualquier campo anotado con @Mock y @InjectMocks.
 */

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    /**
     * El "Porqué" de @Mock:
     * Esto crea un "actor falso" o un "doble" de la interfaz TareaRepository.
     * Este 'tareaRepository' NO es real. No tiene conexión a la base de datos.
     * Es un objeto vacío que podemos programar para que responda como queramos.
     */
    @Mock
    private TaskRepository taskRepository;

    /**
     * El "Porqué" de @InjectMocks:
     * Esto crea una instancia REAL de TareaService.
     * Luego, Mockito mira dentro de TareaService, busca sus dependencias
     * (como 'tareaRepository') y automáticamente "inyecta" los @Mock
     * que definimos arriba.
     *
     */
    @InjectMocks
    private TaskService taskService;

    @Test
    void getTaskPorId_CuandoTaskExiste_DebeDevolverTaskDTO() {

        // --- Given (Dado) ---
        // 1. Preparamos los datos de prueba.
        Long IdSearch = 1L;
        // Esta es la entidad REAL que esperamos que el repositorio devuelva.
        Task tareaEntidad = new Task(IdSearch, "Comprar leche", false);

        // 2. Programamos el Mock!
        // "El porqué": Le decimos a Mockito:
        // "CUANDO (when) alguien llame al metodo 'findById' en nuestro 'tareaRepository'
        // con el argumento '1L', ENTONCES (thenReturn) devuelve un Optional que
        // contiene nuestra 'tareaEntidad'."
        when(taskRepository.findById(IdSearch)).thenReturn(Optional.of(tareaEntidad));

        // --- When (Cuando) ---
        // Llamamos al metodo real en el 'tareaService' (que usa el repo mockeado).
        TaskDto result = taskService.findById(IdSearch);

        // --- Then (Entonces) ---
        // Verificamos que los resultados son los que esperamos.
        // "El porqué": Primero, lo básico. Devolvió algo?
        assertNotNull(result, "El result no debe ser nulo");

        // "El porqué": Ahora, verificamos el contenido.
        // El DTO resultante tiene los mismos datos que la entidad que simulamos?
        assertEquals(IdSearch, result.id(), "El ID no coincide");
        assertEquals("Comprar leche", result.title(), "El título no coincide");
        assertFalse(result.completed(), "El estado 'completada' no coincide");
    }

    @Test
    void getTaskbyId_CuandoTaskNoExiste_DebeLanzarTaskNotFoundException() {

        // --- Given (Dado) ---
        // 1. Definimos un ID que sabemos que no existirá
        Long idInexistente = 99L;

        // 2. Programamos el Mock para el escenario de "no encontrado".
        // "El porqué": Le decimos a Mockito:
        // "CUANDO te pregunten por el ID 99, devuelve un Optional vacío."
        // Esto simula perfectamente el comportamiento de JpaRepository.
        when(taskRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // --- When / Then (Combinado) ---
        // "El porqué": Para probar excepciones, JUnit  nos da 'assertThrows'.
        // Le decimos: "Espero que se lance (assertThrows) una 'TareaNoEncontradaException'
        // CUANDO se ejecute el siguiente bloque de código lambda."

        // El metodo assertThrows devuelve la excepción que fue lanzada.
        // Podemos capturarla para analizarla!
        TaskNotFoundException excepcion = assertThrows(
                TaskNotFoundException.class,
                () -> {
                    // Este es el bloque "When"
                    taskService.findById(idInexistente);
                },
                "Se esperaba que lanzara TareaNoEncontradaException"
        );

        // [Valor Extra] Verificamos el mensaje de la excepción.
        // Esto es crucial para asegurar que damos mensajes de error útiles.
        String mensajeEsperado = "Task not found with id: " + idInexistente;
        assertEquals(mensajeEsperado, excepcion.getMessage(), "El mensaje de la excepción no es el esperado");
    }

    @Test
    void getTareaPorId_CuandoTareaExiste_DebeDevolverTareaDTO_ConAssertJ() {

        // --- Given (Dado) ---
        Long idBuscado = 1L;
        Task tareaEntidad = new Task(idBuscado, "Comprar leche", false);
        when(taskRepository.findById(idBuscado)).thenReturn(Optional.of(tareaEntidad));

        // --- When (Cuando) ---
        TaskDto result = taskService.findById(idBuscado);

        // --- Then (Entonces) con AssertJ ---
        assertThat(result).isNotNull();

        assertThat(result.title())
                .isEqualTo("Comprar leche")
                .startsWith("Compr")
                .endsWith("eche");

        assertThat(result.id()).isEqualTo(idBuscado);
        assertThat(result.completed()).isFalse();
    }

    @Test
    void getTodasLasTareas_CuandoHayDosTareas_DebeDevolverListaDeDTOs() {

        // --- Given (Dado) ---
        Task tarea1 = new Task(1L, "Task 1", false);
        Task tarea2 = new Task(2L, "Task 2", true);
        List<Task> listaEntidades = Arrays.asList(tarea1, tarea2);

        when(taskRepository.findAll()).thenReturn(listaEntidades);

        // --- When (Cuando) ---
        List<TaskDto> listaResultado = taskService.findAll();

        // --- Then (Entonces) ---
        assertThat(listaResultado)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);

        assertThat(listaResultado)
                .extracting(TaskDto::title)
                .containsExactly("Task 1", "Task 2");

        assertThat(listaResultado)
                .extracting(TaskDto::completed)
                .containsExactly(false, true);
    }

    @Test
    void borrarTarea_CuandoSeLlama_DebeInvocarDeleteByIdDelRepositorio() {

        // --- Given (Dado) ---
        Long idABorrar = 1L;

        // --- When (Cuando) ---
        taskService.deleteById(idABorrar);

        // --- Then (Entonces) ---

        verify(taskRepository, times(1)).deleteById(idABorrar);

    }

    @Test
    void crearTarea_ConTituloValido_DebeGuardarYDevolverTarea() {

        // --- Given (Dado) ---
        String titulo = "Mi nueva tarea";

        Task tareaGuardada = new Task(1L, titulo, false);

        when(taskRepository.save(any(Task.class))).thenReturn(tareaGuardada);

        // --- When (Cuando) ---
        TaskDto resultado = taskService.save(new TaskDto(null, titulo, false));

        // --- Then (Entonces) ---
        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1L); // El ID que simulamos
        assertThat(resultado.title()).isEqualTo(titulo);
        assertThat(resultado.completed()).isFalse();
    }

    // "El porqué": Declaramos un Captor a nivel de clase.
    // Le decimos a Mockito: "Prepara una 'caja' para capturar
    // un argumento de tipo Tarea."
    @Captor
    private ArgumentCaptor<Task> tareaArgumentCaptor;

    @Test
    void crearTarea_ConArgumentCaptor_VerificaDatosAntesDeGuardar() {

        // --- Given (Dado) ---
        String titulo = "Tarea con Captor";

        // Para este test, ni siquiera necesitamos mockear el 'save'
        // si solo queremos verificar el argumento.
        // Pero es buena práctica hacerlo para simular el flujo completo.
        Task tareaGuardadaSimulada = new Task(1L, titulo, false);
        when(taskRepository.save(any(Task.class))).thenReturn(tareaGuardadaSimulada);


        // --- When (Cuando) ---
        TaskDto resultado = taskService.save(new TaskDto(null, titulo, false));

        // --- Then (Entonces) ---
        // 1. Verificamos que 'save' fue llamado (como antes)
        // 2. PERO: en lugar de 'any()', le decimos que 'capture()' el argumento.
        verify(taskRepository).save(tareaArgumentCaptor.capture());

        // 3. Ahora, extraemos el valor de la 'caja'!
        Task tareaCapturada = tareaArgumentCaptor.getValue();

        // "El porqué": Esta es la clave.
        // Estamos haciendo asserts sobre el objeto 'Tarea' que
        // el *servicio* creó y pasó al *repositorio*.
        // Tenemos control total!!!
        assertThat(tareaCapturada.getTitle()).isEqualTo(titulo);
        assertThat(tareaCapturada.isCompleted()).isFalse();

        // El ID debe ser nulo ANTES de guardarse
        // (porque es generado por la BD).
        assertThat(tareaCapturada.getId()).isNull();
    }

    @Test
    void deleteById_DebeLlamarSoloADeleteById() {

        // --- Given (Dado) ---
        Long idABorrar = 1L;
        // "El porqué": Para un metodo 'void' como deleteById, no necesitamos
        // usar 'when().thenReturn()'. El mock simplemente aceptará la llamada.
        // Si el metodo 'borrarTarea' en el servicio verificara si existe primero,
        // SÍ necesitaríamos mockear 'findById' aquí.

        // --- When (Cuando) ---
        // Simplemente llamamos al metodo void.
        taskService.deleteById(idABorrar);

        // --- Then (Entonces) ---
        // "El porqué": Aquí está la magia.
        // Le preguntamos a Mockito: "Oye, puedes VERIFICAR (verify)
        // que en el 'tareaRepository', el metodo 'deleteById' fue llamado
        // exactamente 1 vez (times(1)) y que se le pasó el argumento 'idABorrar' (1L)?"
        verify(taskRepository, times(1)).deleteById(idABorrar);
        verifyNoMoreInteractions(taskRepository);
    }

    @Captor
    private ArgumentCaptor<Task> taskArgumentCaptor;

    @Test
    void updateTask_CuandoTareaExiste_DebeActualizarYGuardar() {

        // --- Given (Dado) ---
        Long idExistente = 1L;

        TaskDto dtoEntrada = new TaskDto(idExistente, "Título Actualizado", true);

        Task tareaOriginal = new Task(idExistente, "Título Original", false);

        when(taskRepository.findById(idExistente)).thenReturn(Optional.of(tareaOriginal));

        when(taskRepository.save(any(Task.class))).thenReturn(tareaOriginal);


        // --- When (Cuando) ---
        TaskDto resultadoDto = taskService.updateTask(idExistente, dtoEntrada);

        // --- Then (Entonces) ---

        assertThat(resultadoDto).isNotNull();
        assertThat(resultadoDto.title()).isEqualTo("Título Actualizado");
        assertThat(resultadoDto.completed()).isTrue();

        verify(taskRepository).save(taskArgumentCaptor.capture());
        Task tareaCapturada = taskArgumentCaptor.getValue();

        assertThat(tareaCapturada.getId()).isEqualTo(idExistente);
        assertThat(tareaCapturada.getTitle()).isEqualTo("Título Actualizado");
        assertThat(tareaCapturada.isCompleted()).isTrue();
    }

    @Test
    void markTaskAsCompleted_CuandoTareaExiste_DebePonerTrueYGuardar() {

        // --- Given (Dado) ---
        Long idExistente = 1L;

        Task tareaOriginal = new Task(idExistente, "Hacer la compra", false);

        when(taskRepository.findById(idExistente)).thenReturn(Optional.of(tareaOriginal));

        // --- When (Cuando) ---
        taskService.markTaskAsCompleted(idExistente);

        // --- Then (Entonces) ---

        verify(taskRepository).findById(idExistente);

        verify(taskRepository).save(taskArgumentCaptor.capture());
        Task tareaGuardada = taskArgumentCaptor.getValue();

        assertThat(tareaGuardada.isCompleted()).isTrue();
        assertThat(tareaGuardada.getTitle()).isEqualTo("Hacer la compra");
    }


}
