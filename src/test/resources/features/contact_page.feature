Feature: Contact Page
  As a visitor
  I want to contact the restaurant
  So that I can make inquiries

  Scenario: Visitor sees the contact form
    When I visit the contact page
    Then I should see a 200 status
    And the page should contain "Contact Us"

  Scenario: Visitor submits a contact message
    When I submit a contact message with name "John Doe" email "john@test.com" and message "Great food!"
    Then I should be redirected to the contact page
