Feature: Gallery Page
  As a visitor
  I want to view the photo gallery
  So that I can see the restaurant and food

  Scenario: Visitor views the gallery
    Given there are gallery images in the database
    When I visit the gallery page
    Then I should see a 200 status
    And the page should contain "Our Gallery"

  Scenario: Gallery shows filter buttons
    When I visit the gallery page
    Then the page should contain "All"
    And the page should contain "Food"
    And the page should contain "Ambiance"
