package com.mindhub.homebanking;


import static com.mindhub.homebanking.Utils.CardsUtils.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.mindhub.homebanking.Utils.CardsUtils;
import com.mindhub.homebanking.dtos.CreateCardDto;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ColorType;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.service.CardService;
import com.mindhub.homebanking.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;
import static org.springframework.data.util.Predicates.isFalse;
import static org.springframework.data.util.Predicates.isTrue;
import static org.springframework.test.web.servlet.result.StatusResultMatchersExtensionsKt.isEqualTo;


@SpringBootTest
public class CardServiceTests {

    @MockBean
     ClientService clientService; // Servicio simulado para el cliente

    @MockBean
     Authentication authentication; // Autenticación simulada

    @SpyBean
     CardService cardService; // Usamos Spy para el servicio de tarjeta, para espiar ciertos métodos

    @MockBean
     CardRepository cardRepository; // Repositorio simulado si es que se utiliza para guardar la tarjeta

    @Mock
     CardsUtils cardsUtils; // Usamos Spy para el servicio de CardsUtils

    Client client;//Cliente simulado
    @BeforeEach
    public void setUp() {
        // Inicializamos un cliente simulado
        client = new Client();
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setEmail("john.doe@example.com");

        // Simulación de la autenticación
        when(authentication.getName()).thenReturn(client.getEmail());
        when(clientService.findClientByEmail(anyString())).thenReturn(client);
    }

    //------------------------------------------------------------------------------------
    // Test para el método createCardForClient
//    @Test
//    public void testCreateCardForClient() {
//        CreateCardDto createCardDto = new CreateCardDto("CREDIT", "GOLD"); // Datos de prueba para la creación de la tarjeta
//
//        // Simular validaciones y comportamiento de repositorios
//        when(cardRepository.existsCardsByClientAndColorAndType(any(Client.class), any(ColorType.class), any(CardType.class)))
//                .thenReturn(false); // Para evitar la excepción de límite de tarjetas
//
//        // Simulación del número de tarjeta único
//        when(GenerateCardNumber()).thenReturn("1234-5678-9012-3456");
//
//        cardService.createCardForClient(authentication, createCardDto);
//
//        // Verificar si el método save() fue llamado con el objeto tarjeta creado
//        verify(cardRepository, times(1)).save(any(Card.class));
//
//        // Obtener la tarjeta capturada
//        Card savedCard = cardCaptor.getValue();
//
//        // Verificar que la tarjeta capturada tiene los valores esperados
//        assertThat(savedCard).isNotNull();
//        assertThat(savedCard.getNumber()).isEqualTo("1234-5678-9012-3456");
//        assertThat(savedCard.getType()).isEqualTo(CardType.CREDIT);
//        assertThat(savedCard.getColor()).isEqualTo(ColorType.GOLD);
//        assertThat(savedCard.getClient()).isEqualTo(client);
//    }


    //------------------------------------------------------------------------------------
    // Test para el método exitsCardByClientAndColorAndType
    @Test
    public void testExitsCardByClientAndColorAndType() {
        // Simulación de que existe una tarjeta con un color y tipo específico para el cliente
        when(cardRepository.existsCardsByClientAndColorAndType(client, ColorType.GOLD, CardType.CREDIT))
                .thenReturn(true);

        Boolean exists = cardService.exitsCardByClientAndColorAndType(client, ColorType.GOLD, CardType.CREDIT);

        // Verificar el resultado esperado
        assertThat(exists,is(true));
    }


    //------------------------------------------------------------------------------------
    // Test para el método saveCard
    @Test
    public void testSaveCard() {
        Card card = new Card();
        card.setNumber("1234-5678-9012-3456");

        // Simulación del comportamiento del repositorio
        when(cardRepository.save(card)).thenReturn(card);

        Card savedCard = cardService.saveCard(card);

        // Verificación
        assertThat(savedCard,notNullValue());
        assertThat(savedCard.getNumber(),equalTo("1234-5678-9012-3456"));
    }


    //------------------------------------------------------------------------------------
    // Test para el método buildCard
    @Test
    public void testBuildCard() {
        CreateCardDto createCardDto = new CreateCardDto("CREDIT", "GOLD");


//        // Simulación del número de tarjeta y otros valores
//        when(cardsUtils.GenerateCardNumber()).thenReturn("1234-5678-9012-3456");
//        when(cardsUtils.generarCodigoSeguridad()).thenReturn(123);
//        when(cardsUtils.generateFromDate()).thenReturn(LocalDate.now());
//        when(cardsUtils.generateExpirationDate()).thenReturn(LocalDate.now().plusYears(5));


        Card card = cardService.buildCard(client, createCardDto);

        // Verificación de que la tarjeta fue creada correctamente
        assertThat(card,notNullValue());
        assertThat(card.getClient(),equalTo(client));
//        assertThat(card.getNumber(),equalTo("1234-5678-9012-3456"));
        assertThat(card.getType(),equalTo(CardType.CREDIT));
        assertThat(card.getColor(),equalTo(ColorType.GOLD));
    }



    //------------------------------------------------------------------------------------
    // Test para el método generateUniqueCardNumber
    @Test
    public void testGenerateUniqueCardNumber() {
        // Simulación del número de tarjeta y que no existe en la base de datos
//        when(GenerateCardNumber()).thenReturn("1234-5678-9012-3456");
        when(cardRepository.existsCardNumberByNumber("1234-5678-9012-3456")).thenReturn(false);

        String cardNumber = cardService.generateUniqueCardNumber();

        // Verificar que el número de tarjeta
        assertThat(cardNumber,notNullValue());
    }


    //------------------------------------------------------------------------------------
    // Test para el método validateCardCreation
    @Test
    public void testValidateCardCreation_validData() {
        CreateCardDto createCardDto = new CreateCardDto("CREDIT", "GOLD");

        // Este método no tiene retorno, solo validamos que no lance excepción con datos correctos
        assertThatCode(() -> cardService.validateCardCreation(createCardDto))
                .doesNotThrowAnyException();
    }

    @Test
    public void testValidateCardCreation_invalidType() {
        CreateCardDto createCardDto = new CreateCardDto("INVALID", "GOLD");

        // Validar que se lance una excepción con un tipo inválido
        assertThatThrownBy(() -> cardService.validateCardCreation(createCardDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid card type");
    }

    //------------------------------------------------------------------------------------
    // Test para el método checkCardLimit
    @Test
    public void testCheckCardLimit_exceedsLimit() {
        CreateCardDto createCardDto = new CreateCardDto("CREDIT", "GOLD");

        // Simular que el cliente ya tiene una tarjeta del mismo tipo y color
        when(cardRepository.existsCardsByClientAndColorAndType(
                client,
                ColorType.GOLD,
                CardType.CREDIT))
                .thenReturn(true);


        // Verificar que se lance una excepción cuando se excede el límite de tarjetas
        assertThatThrownBy(() -> cardService.checkCardLimit(client, createCardDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("You can't have more than one");
    }



}
