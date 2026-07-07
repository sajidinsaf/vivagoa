package com.vivagoa.config;

import com.vivagoa.entity.AdminUser;
import com.vivagoa.entity.ContactInfo;
import com.vivagoa.entity.GalleryImage;
import com.vivagoa.entity.MenuItem;
import com.vivagoa.repository.ContactInfoRepository;
import com.vivagoa.repository.GalleryImageRepository;
import com.vivagoa.repository.MenuItemRepository;
import com.vivagoa.service.AdminUserService;
import com.vivagoa.service.GalleryService;
import com.vivagoa.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final AdminUserService adminUserService;
    private final PasswordEncoder passwordEncoder;
    private final ContactInfoRepository contactInfoRepository;
    private final GalleryService galleryService;
    private final MenuService menuService;
    private final GalleryImageRepository galleryImageRepository;
    private final MenuItemRepository menuItemRepository;

    public DataInitializer(AdminUserService adminUserService,
                           PasswordEncoder passwordEncoder,
                           ContactInfoRepository contactInfoRepository,
                           GalleryService galleryService,
                           MenuService menuService,
                           GalleryImageRepository galleryImageRepository,
                           MenuItemRepository menuItemRepository) {
        this.adminUserService = adminUserService;
        this.passwordEncoder = passwordEncoder;
        this.contactInfoRepository = contactInfoRepository;
        this.galleryService = galleryService;
        this.menuService = menuService;
        this.galleryImageRepository = galleryImageRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public void run(String... args) {
        initAdminUser();
        initContactInfo();
        initGalleryImages();
        initMenuItems();
    }

    private void initAdminUser() {
        if (adminUserService.count() == 0) {
            AdminUser admin = new AdminUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            adminUserService.save(admin);
            logger.info("Default admin user created (admin/admin123)");
        }
    }

    private void initContactInfo() {
        if (contactInfoRepository.findFirstBy().isEmpty()) {
            ContactInfo info = new ContactInfo();
            info.setPhone1("9822483869");
            info.setPhone2("7447446575");
            info.setEmail("service@vivagoa.visibleai.com");
            info.setAddress("Sernabatim Road, near Lions Club, Colva, Goa 403708");
            info.setOpeningHours("Open Daily: 11:00 AM - 11:00 PM");
            info.setInstagramUrl("https://www.instagram.com/vivagoa_colva/");
            info.setFacebookUrl("https://www.facebook.com/vivagoacolva/");
            info.setZomatoUrl("https://www.zomato.com/goa/viva-goa-colva");
            contactInfoRepository.save(info);
            logger.info("Default contact info created");
        }
    }

    private void initGalleryImages() {
        if (galleryImageRepository.count() == 0) {
            int order = 1;
            seedGalleryImage("logo.jpg", "Viva Goa Logo", "The iconic Viva Goa logo", GalleryImage.Category.AMBIANCE, order++);
            seedGalleryImage("dining1.jpg", "Al Fresco Dining", "Open-air dining area with views of the Colva countryside", GalleryImage.Category.AMBIANCE, order++);
            seedGalleryImage("dining2.jpg", "Indoor Dining", "Cozy indoor dining with artistic wall murals and wooden ceiling", GalleryImage.Category.AMBIANCE, order++);
            seedGalleryImage("evening.jpg", "Evening Ambiance", "Viva Goa lit up at night - warm lanterns and inviting atmosphere", GalleryImage.Category.AMBIANCE, order++);
            seedGalleryImage("vivabar.jpg", "The Bar", "Rustic brick bar stocked with premium spirits and cocktails", GalleryImage.Category.AMBIANCE, order++);
            seedGalleryImage("markettotable.jpg", "Market to Table", "Fresh catch sourced daily from local Goan fish markets", GalleryImage.Category.FOOD, order++);
            seedGalleryImage("barcode.jpg", "Find Us", "Scan to locate Viva Goa - next to Lions Club, Colva", GalleryImage.Category.AMBIANCE, order++);
            seedGalleryImage("IMG_9320.jpeg", "Prawns Curry Rice", "Succulent Goan prawns in rich coconut curry served with steamed rice and mashed potatoes", GalleryImage.Category.FOOD, order++);
            seedGalleryImage("IMG_9325.jpeg", "Steak with Egg", "Sizzling beef steak topped with a fried egg, served with mashed potatoes and sauteed vegetables", GalleryImage.Category.FOOD, order++);
            seedGalleryImage("IMG_9326.jpeg", "Pepper Steak", "Juicy pepper steak topped with fried egg, creamy mashed potatoes and fresh vegetables", GalleryImage.Category.FOOD, order++);
            seedGalleryImage("IMG_9327.jpeg", "Steak & Milkshake", "Classic steak platter with fried egg, vegetables and a chocolate milkshake", GalleryImage.Category.FOOD, order++);
            seedGalleryImage("IMG_9345.jpeg", "Chicken Handi", "Steaming chicken handi baked in foil with rice, fries and buttered vegetables", GalleryImage.Category.FOOD, order++);
            seedGalleryImage("IMG_9346.jpeg", "Sizzling Handi", "Piping hot handi served with aromatic basmati rice and crispy french fries", GalleryImage.Category.FOOD, order++);
            seedGalleryImage("IMG_9363.jpeg", "Seafood Feast", "Goan masala fish fry with chilli chicken dry and crispy prawn crackers", GalleryImage.Category.FOOD, order++);
            seedGalleryImage("IMG_9364.jpeg", "Kingfish Masala Fry", "Perfectly spiced Goan-style kingfish fry with fresh salad and lime", GalleryImage.Category.FOOD, order++);
            seedGalleryImage("IMG_9365.jpeg", "Chilli Chicken Dry", "Wok-tossed chicken with peppers and onions in a spicy soy glaze", GalleryImage.Category.FOOD, order++);
            seedGalleryImage("IMG_9366.jpeg", "Prawn Crackers", "Light and crispy prawn crackers - the perfect appetizer", GalleryImage.Category.FOOD, order++);
            seedGalleryImage("IMG_9367.jpeg", "Steak Continental", "Tender steak with fried egg, mashed potatoes and steamed vegetables", GalleryImage.Category.FOOD, order++);
            seedGalleryImage("IMG_9368.jpeg", "Beef Steak Platter", "Grilled beef steak with egg, creamy mash, and garden-fresh vegetables", GalleryImage.Category.FOOD, order++);
            logger.info("Gallery images seeded: {} images", order - 1);
        }
    }

    private void seedGalleryImage(String fileName, String title, String description, GalleryImage.Category category, int order) {
        GalleryImage image = new GalleryImage();
        image.setFileName(fileName);
        image.setTitle(title);
        image.setDescription(description);
        image.setCategory(category);
        image.setDisplayOrder(order);
        image.setActive(true);
        galleryService.save(image);
    }

    private void initMenuItems() {
        if (menuItemRepository.count() == 0) {
            int order = 1;

            // STARTERS
            order = seedMenuCategory("Starters", order,
                new Object[]{"Golden Fry Prawns", 440, false},
                new Object[]{"Hot Garlic Wings", 260, false},
                new Object[]{"Fish Fingers", 390, false},
                new Object[]{"Calamari Batter / Masala Fry", 390, false},
                new Object[]{"Calamari Butter Garlic", 390, false},
                new Object[]{"Crispy Chicken", 260, false},
                new Object[]{"Chicken Lollipop", 200, false},
                new Object[]{"Chilli Chicken", 260, false},
                new Object[]{"Chicken 65 / Garlic Chicken", 260, false},
                new Object[]{"Chicken Schezwan", 260, false},
                new Object[]{"Chicken Manchurian", 260, false},
                new Object[]{"Chicken Dry Fry", 260, false},
                new Object[]{"Garlic Prawns", 440, false},
                new Object[]{"Chilli Fish", 400, false},
                new Object[]{"Cheese Garlic Chips", 200, true},
                new Object[]{"French Fries / Chips", 150, true},
                new Object[]{"Paneer Chilly / 65", 260, true},
                new Object[]{"Mushroom Wine Garlic", 260, true},
                new Object[]{"Mushroom Chilly / Manchurian", 240, true},
                new Object[]{"Gobi 65 / Manchurian", 220, true},
                new Object[]{"Veg Cutlets", 200, true}
            );

            // SALAD
            order = seedMenuCategory("Salad", order,
                new Object[]{"Green Salad", 150, true},
                new Object[]{"Chicken Pasta Salad", 200, false},
                new Object[]{"Prawns Pasta Salad", 250, false},
                new Object[]{"Curd", 120, true},
                new Object[]{"Mix Raita", 120, true}
            );

            // GOAN DELICACIES
            order = seedMenuCategory("Goan Delicacies", order,
                new Object[]{"Chicken Cafreal", 330, false},
                new Object[]{"Chicken Chilly Fry (Goan)", 260, false},
                new Object[]{"Chicken Xacuti (Gravy)", 290, false},
                new Object[]{"Chicken Vindaloo", 290, false},
                new Object[]{"Chicken Curry", 290, false},
                new Object[]{"Chicken Stew", 290, false}
            );

            // SEAFOOD
            order = seedMenuCategory("Seafood", order,
                new Object[]{"Prawns Curry Rice", 440, false},
                new Object[]{"Kingfish Curry Rice 2 Pcs", 0, false},
                new Object[]{"Pomfret Curry Rice", 0, false},
                new Object[]{"Mackerel Curry Rice", 250, false},
                new Object[]{"Shark Ambotik Rice", 0, false},
                new Object[]{"Kingfish Cafreal", 0, false},
                new Object[]{"Prawns Goan Masala Fry", 0, false},
                new Object[]{"Prawns Rava Fry", 0, false},
                new Object[]{"Prawns Chilly Fry (Goan)", 0, false},
                new Object[]{"Bombay Duck / Lepo", 0, false},
                new Object[]{"Kingfish Masala Fry 2 Fillet", 0, false},
                new Object[]{"Kingfish Rava Fry 2 Fillet", 0, false},
                new Object[]{"Pomfret Rechado", 0, false},
                new Object[]{"Mackerel Rechado (Bangda)", 220, false},
                new Object[]{"Mackerel Rava Fry (Bangda)", 220, false}
            );

            // INDIAN (BONELESS)
            order = seedMenuCategory("Indian", order,
                new Object[]{"Chicken Tikka Masala", 300, false},
                new Object[]{"Butter Chicken (Boneless)", 290, false},
                new Object[]{"Chicken Kadai (Boneless)", 290, false},
                new Object[]{"Tawa Chicken (Boneless)", 290, false},
                new Object[]{"Chicken Hariyali (Boneless)", 290, false},
                new Object[]{"Chicken Masala (Boneless)", 290, false},
                new Object[]{"Chicken Kolhapuri (Boneless)", 290, false},
                new Object[]{"Chicken Palak (Boneless)", 290, false},
                new Object[]{"Chicken Handi (Boneless)", 290, false},
                new Object[]{"Chicken Jalfrazie (Boneless)", 290, false},
                new Object[]{"Chicken Mughlai (Boneless)", 290, false},
                new Object[]{"Mutton Masala", 440, false},
                new Object[]{"Mutton Xacuti (Goan)", 440, false},
                new Object[]{"Mutton Rogan Josh", 440, false}
            );

            // VEG MAIN COURSE
            order = seedMenuCategory("Veg Main Course", order,
                new Object[]{"Paneer Butter Masala", 280, true},
                new Object[]{"Paneer Palak", 280, true},
                new Object[]{"Paneer Kadai", 280, true},
                new Object[]{"Malai Kofta", 280, true},
                new Object[]{"Mushroom Do Pyaza", 280, true},
                new Object[]{"Mushroom Mutter Masala", 280, true},
                new Object[]{"Veg Kadai", 250, true},
                new Object[]{"Veg Jalfrazie", 250, true},
                new Object[]{"Aloo Gobi", 250, true},
                new Object[]{"Aloo Mutter", 250, true},
                new Object[]{"Mutter Gobi", 250, true},
                new Object[]{"Veg Kolhapuri", 250, true},
                new Object[]{"Aloo Palak", 250, true},
                new Object[]{"Tomato Palak", 250, true},
                new Object[]{"Veg Kurma", 250, true},
                new Object[]{"Bhindi Masala", 250, true},
                new Object[]{"Dal Tadka", 250, true},
                new Object[]{"Dal Fry", 180, true}
            );

            // NOODLES
            order = seedMenuCategory("Noodles", order,
                new Object[]{"Veg Hakka / Noodles", 190, true},
                new Object[]{"Chicken Hakka / Noodles", 200, false},
                new Object[]{"Prawns Hakka / Noodles", 250, false},
                new Object[]{"Mix Hakka / Noodles", 200, false}
            );

            // DELIVERY MENU - Non Vegetarian
            order = seedMenuCategory("Delivery - Non Vegetarian", order,
                new Object[]{"Butter Chicken", 250, false},
                new Object[]{"Chicken Tikka Masala", 250, false},
                new Object[]{"Chicken Kolhapuri", 250, false},
                new Object[]{"Kadai Chicken", 250, false},
                new Object[]{"Chicken 65", 250, false},
                new Object[]{"Chilly Chicken", 250, false},
                new Object[]{"Crispy Chicken", 250, false},
                new Object[]{"Chicken Manchurian", 250, false},
                new Object[]{"Garlic Chicken", 250, false},
                new Object[]{"Chicken Ala King", 290, false},
                new Object[]{"Chicken Hakka Noodles", 180, false}
            );

            // DELIVERY MENU - Vegetarian
            order = seedMenuCategory("Delivery - Vegetarian", order,
                new Object[]{"Dal Fry", 150, true},
                new Object[]{"Veg Kadai", 200, true},
                new Object[]{"Gobi Manchurian", 200, true},
                new Object[]{"Paneer Chilly", 250, true},
                new Object[]{"Paneer Kadai", 250, true},
                new Object[]{"Paneer Butter Masala", 250, true},
                new Object[]{"Chaana Masala", 200, true},
                new Object[]{"Aloo Gobi", 200, true},
                new Object[]{"Veg Hakka Noodles", 180, true},
                new Object[]{"Chips", 120, true},
                new Object[]{"Veg Crispy", 180, true}
            );

            // DELIVERY MENU - Rice
            order = seedMenuCategory("Delivery - Rice", order,
                new Object[]{"Steam Rice", 80, true},
                new Object[]{"Jeera Rice", 150, true},
                new Object[]{"Veg Fried Rice", 180, true},
                new Object[]{"Chicken Fried Rice", 180, false},
                new Object[]{"Veg Biryani", 200, true},
                new Object[]{"Chicken Biryani", 250, false},
                new Object[]{"Sausage Pulao", 200, false},
                new Object[]{"Veg Pulao", 150, true},
                new Object[]{"Prawn Fried Rice", 200, false},
                new Object[]{"Mix Fried Rice", 200, false}
            );

            // DELIVERY MENU - Soup & Extras
            order = seedMenuCategory("Delivery - Soup & Extras", order,
                new Object[]{"Manchow Soup (Veg/Chicken)", 150, true},
                new Object[]{"Hot & Sour Soup (Veg/Chicken)", 150, true},
                new Object[]{"Chapati", 20, true}
            );

            logger.info("Menu items seeded: {} items", order - 1);
        }
    }

    private int seedMenuCategory(String category, int startOrder, Object[]... items) {
        int order = startOrder;
        for (Object[] item : items) {
            String name = (String) item[0];
            int price = (int) item[1];
            boolean vegetarian = (boolean) item[2];

            MenuItem menuItem = new MenuItem();
            menuItem.setName(name);
            menuItem.setCategory(category);
            menuItem.setPrice(BigDecimal.valueOf(price));
            menuItem.setVegetarian(vegetarian);
            menuItem.setAvailable(true);
            menuItem.setDisplayOrder(order++);

            // For items with price 0, set description
            if (price == 0) {
                menuItem.setDescription("As Per Size");
            }

            menuService.save(menuItem);
        }
        return order;
    }
}
