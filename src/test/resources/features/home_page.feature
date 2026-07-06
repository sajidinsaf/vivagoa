Feature: Home Page
  As a visitor
  I want to see the restaurant home page
  So that I can learn about Viva Goa

  Scenario: Visitor sees the home page
    When I visit the home page
    Then I should see a 200 status
    And the page should contain "VIVA GOA"
    And the page should contain "Food is an experience"

  Scenario: Home page displays featured dishes
    Given there are gallery images in the database
    When I visit the home page
    Then I should see a 200 status

  Scenario: Home page shows reviews carousel
    When I visit the home page
    Then the page should contain "What Our Guests Say"
    And the page should contain "Reviews"
