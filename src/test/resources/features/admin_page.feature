Feature: Admin Dashboard
  As an admin
  I want to manage the restaurant website
  So that I can handle bookings and content

  Scenario: Unauthenticated user cannot access admin
    When I try to access the admin dashboard without logging in
    Then I should be redirected to the login page

  Scenario: Admin can view the dashboard
    Given I am logged in as admin
    When I visit the admin dashboard
    Then I should see a 200 status

  Scenario: Admin can view reservations
    Given I am logged in as admin
    When I visit the admin reservations page
    Then I should see a 200 status

  Scenario: Admin can view gallery management
    Given I am logged in as admin
    When I visit the admin gallery page
    Then I should see a 200 status

  Scenario: Admin can view messages
    Given I am logged in as admin
    When I visit the admin messages page
    Then I should see a 200 status

  Scenario: Admin can update reservation status
    Given I am logged in as admin
    And a reservation exists in the database
    When I update the reservation status to "CANCELLED"
    Then I should be redirected to admin reservations
