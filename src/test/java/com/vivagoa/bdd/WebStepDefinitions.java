package com.vivagoa.bdd;

import com.vivagoa.entity.GalleryImage;
import com.vivagoa.entity.MenuItem;
import com.vivagoa.entity.Reservation;
import com.vivagoa.repository.GalleryImageRepository;
import com.vivagoa.repository.MenuItemRepository;
import com.vivagoa.repository.ReservationRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class WebStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GalleryImageRepository galleryImageRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private MvcResult mvcResult;
    private ResultActions resultActions;
    private boolean loggedInAsAdmin = false;
    private Long testReservationId;

    @Before
    public void setUp() {
        loggedInAsAdmin = false;
        testReservationId = null;
    }

    // ========== GIVEN steps ==========

    @Given("there are gallery images in the database")
    public void thereAreGalleryImagesInTheDatabase() {
        if (galleryImageRepository.count() == 0) {
            GalleryImage img1 = new GalleryImage();
            img1.setFileName("test-food.jpg");
            img1.setTitle("Test Food Image");
            img1.setDescription("A delicious test dish");
            img1.setCategory(GalleryImage.Category.FOOD);
            img1.setDisplayOrder(1);
            img1.setActive(true);
            galleryImageRepository.save(img1);

            GalleryImage img2 = new GalleryImage();
            img2.setFileName("test-ambiance.jpg");
            img2.setTitle("Test Ambiance Image");
            img2.setDescription("Beautiful restaurant setting");
            img2.setCategory(GalleryImage.Category.AMBIANCE);
            img2.setDisplayOrder(2);
            img2.setActive(true);
            galleryImageRepository.save(img2);

            GalleryImage img3 = new GalleryImage();
            img3.setFileName("test-event.jpg");
            img3.setTitle("Test Event Image");
            img3.setDescription("Special event at the restaurant");
            img3.setCategory(GalleryImage.Category.EVENTS);
            img3.setDisplayOrder(3);
            img3.setActive(true);
            galleryImageRepository.save(img3);
        }
    }

    @Given("menu items exist in the database")
    public void menuItemsExistInTheDatabase() {
        if (menuItemRepository.count() == 0) {
            MenuItem starter = new MenuItem();
            starter.setName("Golden Fry Prawns");
            starter.setDescription("Crispy fried prawns");
            starter.setPrice(BigDecimal.valueOf(440));
            starter.setCategory("Starters");
            starter.setVegetarian(false);
            starter.setAvailable(true);
            starter.setDisplayOrder(1);
            menuItemRepository.save(starter);

            MenuItem seafood = new MenuItem();
            seafood.setName("Prawns Curry Rice");
            seafood.setDescription("Goan prawns curry with rice");
            seafood.setPrice(BigDecimal.valueOf(440));
            seafood.setCategory("Seafood");
            seafood.setVegetarian(false);
            seafood.setAvailable(true);
            seafood.setDisplayOrder(2);
            menuItemRepository.save(seafood);

            MenuItem goan = new MenuItem();
            goan.setName("Chicken Cafreal");
            goan.setDescription("Traditional Goan chicken dish");
            goan.setPrice(BigDecimal.valueOf(330));
            goan.setCategory("Goan Delicacies");
            goan.setVegetarian(false);
            goan.setAvailable(true);
            goan.setDisplayOrder(3);
            menuItemRepository.save(goan);

            MenuItem vegItem = new MenuItem();
            vegItem.setName("Paneer Butter Masala");
            vegItem.setDescription("Creamy paneer dish");
            vegItem.setPrice(BigDecimal.valueOf(280));
            vegItem.setCategory("Veg Main Course");
            vegItem.setVegetarian(true);
            vegItem.setAvailable(true);
            vegItem.setDisplayOrder(4);
            menuItemRepository.save(vegItem);
        }
    }

    @Given("I am logged in as admin")
    public void iAmLoggedInAsAdmin() {
        loggedInAsAdmin = true;
    }

    @Given("a reservation exists in the database")
    public void aReservationExistsInTheDatabase() {
        Reservation reservation = new Reservation();
        reservation.setGuestName("Test Guest");
        reservation.setEmail("test@example.com");
        reservation.setPhone("9876543210");
        reservation.setPartySize(2);
        reservation.setReservationDate(LocalDate.now().plusDays(1));
        reservation.setReservationTime(LocalTime.of(19, 0));
        reservation.setStatus(Reservation.Status.CONFIRMED);
        Reservation saved = reservationRepository.save(reservation);
        testReservationId = saved.getId();
    }

    // ========== WHEN steps ==========

    @When("I visit the home page")
    public void iVisitTheHomePage() throws Exception {
        resultActions = mockMvc.perform(get("/"));
        mvcResult = resultActions.andReturn();
    }

    @When("I visit the menu page")
    public void iVisitTheMenuPage() throws Exception {
        resultActions = mockMvc.perform(get("/menu"));
        mvcResult = resultActions.andReturn();
    }

    @When("I visit the gallery page")
    public void iVisitTheGalleryPage() throws Exception {
        resultActions = mockMvc.perform(get("/gallery"));
        mvcResult = resultActions.andReturn();
    }

    @When("I visit the contact page")
    public void iVisitTheContactPage() throws Exception {
        resultActions = mockMvc.perform(get("/contact"));
        mvcResult = resultActions.andReturn();
    }

    @When("I visit the reservation page")
    public void iVisitTheReservationPage() throws Exception {
        resultActions = mockMvc.perform(get("/reservations"));
        mvcResult = resultActions.andReturn();
    }

    @When("I submit a contact message with name {string} email {string} and message {string}")
    public void iSubmitAContactMessage(String name, String email, String message) throws Exception {
        resultActions = mockMvc.perform(post("/contact")
                .with(csrf())
                .param("name", name)
                .param("email", email)
                .param("message", message));
        mvcResult = resultActions.andReturn();
    }

    @When("I submit a reservation for {string} email {string} phone {string} party size {int}")
    public void iSubmitAReservation(String name, String email, String phone, int partySize) throws Exception {
        resultActions = mockMvc.perform(post("/reservations")
                .with(csrf())
                .param("guestName", name)
                .param("email", email)
                .param("phone", phone)
                .param("partySize", String.valueOf(partySize))
                .param("reservationDate", LocalDate.now().plusDays(1).toString())
                .param("reservationTime", "19:00"));
        mvcResult = resultActions.andReturn();
    }

    @When("I try to access the admin dashboard without logging in")
    public void iTryToAccessAdminDashboardWithoutLoggingIn() throws Exception {
        resultActions = mockMvc.perform(get("/admin"));
        mvcResult = resultActions.andReturn();
    }

    @When("I visit the admin dashboard")
    public void iVisitTheAdminDashboard() throws Exception {
        resultActions = mockMvc.perform(get("/admin")
                .with(user("admin").roles("ADMIN")));
        mvcResult = resultActions.andReturn();
    }

    @When("I visit the admin reservations page")
    public void iVisitTheAdminReservationsPage() throws Exception {
        resultActions = mockMvc.perform(get("/admin/reservations")
                .with(user("admin").roles("ADMIN")));
        mvcResult = resultActions.andReturn();
    }

    @When("I visit the admin gallery page")
    public void iVisitTheAdminGalleryPage() throws Exception {
        resultActions = mockMvc.perform(get("/admin/gallery")
                .with(user("admin").roles("ADMIN")));
        mvcResult = resultActions.andReturn();
    }

    @When("I visit the admin messages page")
    public void iVisitTheAdminMessagesPage() throws Exception {
        resultActions = mockMvc.perform(get("/admin/messages")
                .with(user("admin").roles("ADMIN")));
        mvcResult = resultActions.andReturn();
    }

    @When("I update the reservation status to {string}")
    public void iUpdateTheReservationStatusTo(String status) throws Exception {
        resultActions = mockMvc.perform(post("/admin/reservations/" + testReservationId + "/status")
                .with(user("admin").roles("ADMIN"))
                .with(csrf())
                .param("status", status));
        mvcResult = resultActions.andReturn();
    }

    // ========== THEN steps ==========

    @Then("I should see a {int} status")
    public void iShouldSeeAStatus(int statusCode) throws Exception {
        resultActions.andExpect(status().is(statusCode));
    }

    @Then("the page should contain {string}")
    public void thePageShouldContain(String text) throws Exception {
        resultActions.andExpect(content().string(containsString(text)));
    }

    @Then("I should be redirected to the contact page")
    public void iShouldBeRedirectedToTheContactPage() throws Exception {
        resultActions.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contact"));
    }

    @Then("I should be redirected to the reservation page")
    public void iShouldBeRedirectedToTheReservationPage() throws Exception {
        resultActions.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reservations"));
    }

    @Then("I should be redirected to the login page")
    public void iShouldBeRedirectedToTheLoginPage() throws Exception {
        resultActions.andExpect(status().is3xxRedirection());
        String redirectUrl = mvcResult.getResponse().getRedirectedUrl();
        assert redirectUrl != null && redirectUrl.contains("/admin/login");
    }

    @Then("I should be redirected to admin reservations")
    public void iShouldBeRedirectedToAdminReservations() throws Exception {
        resultActions.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/reservations"));
    }
}
