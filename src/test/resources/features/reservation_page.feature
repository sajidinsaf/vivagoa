Feature: Reservation Page
  As a visitor
  I want to make a table reservation
  So that I can book a dining experience

  Scenario: Visitor sees the reservation form
    When I visit the reservation page
    Then I should see a 200 status
    And the page should contain "Reserve a Table"

  Scenario: Visitor makes a reservation
    When I submit a reservation for "Jane Doe" email "jane@test.com" phone "9876543210" party size 4
    Then I should be redirected to the reservation page
