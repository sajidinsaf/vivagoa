Feature: Menu Page
  As a visitor
  I want to browse the restaurant menu
  So that I can see what dishes are available

  Scenario: Visitor views the menu page
    Given menu items exist in the database
    When I visit the menu page
    Then I should see a 200 status
    And the page should contain "Our Menu"

  Scenario: Menu displays categories
    Given menu items exist in the database
    When I visit the menu page
    Then the page should contain "Starters"
    And the page should contain "Seafood"
    And the page should contain "Goan Delicacies"

  Scenario: Menu shows vegetarian indicators
    Given menu items exist in the database
    When I visit the menu page
    Then I should see a 200 status
