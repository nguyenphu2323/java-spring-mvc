package vn.hoidanit.laptopshop.controller.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import vn.hoidanit.laptopshop.domain.Cart;
import vn.hoidanit.laptopshop.domain.CartDetail;
import vn.hoidanit.laptopshop.domain.Order;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.Product_;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.domain.dto.ProductCriteriaDTO;
import vn.hoidanit.laptopshop.service.CartDetailService;
import vn.hoidanit.laptopshop.service.OrderService;
import vn.hoidanit.laptopshop.service.ProductService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

public class ItemController {

    private final ProductService productService;
    private final CartDetailService cartDetailService;
    private final OrderService orderService;

    public ItemController(ProductService productService, CartDetailService cartDetailService,
            OrderService orderService) {
        this.productService = productService;
        this.cartDetailService = cartDetailService;
        this.orderService = orderService;
    }

    @GetMapping("/product/{id}")
    public String getProductDetailPage(org.springframework.ui.Model model, @PathVariable long id) {
        Product product = this.productService.getProductByID(id).get();
        model.addAttribute("product", product);
        model.addAttribute("id", id);
        return "client/product/detail";
    }

    @PostMapping("add-product-to-card/{id}")
    public String addProductToCard(@PathVariable long id, HttpServletRequest request) {
        // TODO: process POST request
        HttpSession session = request.getSession(false);
        long productId = id;
        String email = (String) session.getAttribute("email");
        this.productService.handleAddProductToCart(email, productId, session, 1);
        return "redirect:/";
    }

    @GetMapping("/cart")
    public String getCartPage(Model model, HttpServletRequest request) {
        User currentUser = new User();
        HttpSession session = request.getSession(false);
        long id = (long) session.getAttribute("id");
        currentUser.setId(id);

        Cart cart = this.productService.fetchByUser(currentUser);
        List<CartDetail> cartDetails = cart == null ? new ArrayList<CartDetail>() : cart.getCartDetails(); // Lấy ra
                                                                                                           // cart
                                                                                                           // detail

        double totalPrice = 0;
        for (CartDetail cd : cartDetails) {
            totalPrice += cd.getPrice() * cd.getQuantity();
        }
        model.addAttribute("cartDetails", cartDetails);
        model.addAttribute("totalPrice", totalPrice);

        model.addAttribute("cart", cart);
        return "client/cart/show";
    }

    @PostMapping("/delete-cart-product/{id}")
    public String deleteCart(Model model, @PathVariable long id, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        long cartDetail = id;
        this.productService.deleteCartDetailById(cartDetail, session);
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String getCheckOutPage(Model model, HttpServletRequest request) {
        User currentUser = new User();
        HttpSession session = request.getSession(false);
        long id = (long) session.getAttribute("id");
        currentUser.setId(id);

        Cart cart = this.productService.fetchByUser(currentUser);
        List<CartDetail> cartDetails = cart == null ? new ArrayList<CartDetail>() : cart.getCartDetails(); // Lấy ra
                                                                                                           // cart
                                                                                                           // detail

        double totalPrice = 0;
        for (CartDetail cd : cartDetails) {
            totalPrice += cd.getPrice() * cd.getQuantity();
        }
        model.addAttribute("cartDetails", cartDetails);
        model.addAttribute("totalPrice", totalPrice);

        return "client/cart/checkout";
    }

    @PostMapping("/confirm-checkout")
    public String getCheckoutPage(@ModelAttribute("cart") Cart cart) {
        List<CartDetail> cartDetails = cart == null ? new ArrayList<CartDetail>() : cart.getCartDetails();
        this.productService.handelUpdateCartBeforeCheckout(cartDetails);

        return "redirect:/checkout";
    }

    @PostMapping("/place-order")
    public String handlePlaceOrder(HttpServletRequest request,
            @RequestParam("receiverName") String receiverName,
            @RequestParam("receiverAddress") String receiverAddress,
            @RequestParam("receiverPhone") String receiverPhone) {
        // TODO: process POST request
        User currentUser = new User();
        HttpSession session = request.getSession(false);
        long id = (long) session.getAttribute("id");
        currentUser.setId(id);

        this.productService.handlePlaceOrder(currentUser, session, receiverName, receiverPhone, receiverAddress);
        return "redirect:/thank";
    }

    @GetMapping("/thank")
    public String getThankYouPage(Model model) {
        return "client/cart/thank";
    }

    @PostMapping("/add-product-from-view-detail")
    public String handleAddProductFromViewDetail(
            @RequestParam("id") long id,
            @RequestParam("quantity") long quantity,
            HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String email = (String) session.getAttribute("email");
        this.productService.handleAddProductToCart(email, id, session, quantity);
        return "redirect:/product/" + id;
    }

    @GetMapping("/products")
    public String getProductPage(Model model, ProductCriteriaDTO productCriteriaDTO,
            HttpServletRequest request) {
        int page = 1;
        try {
            if (productCriteriaDTO.getPage().isPresent()) {
                page = Integer.parseInt(productCriteriaDTO.getPage().get());
            } else {
                // page=1
            }
        } catch (Exception e) {
            // TODO: handle exception
            // page=1
        }
        // check sort price
        Pageable pageable = PageRequest.of(page - 1, 3);
        if (productCriteriaDTO.getSort() != null && productCriteriaDTO.getSort().isPresent()) {
            String sort = productCriteriaDTO.getSort().get();
            if (sort.equals("gia-tang-dan")) {
                pageable = PageRequest.of(page - 1, 10, Sort.by(Product_.PRICE).ascending());
            } else if (sort.equals("gia-giam-dan")) {
                pageable = PageRequest.of(page - 1, 10, Sort.by(Product_.PRICE).descending());
            } else {
                pageable = PageRequest.of(page - 1, 10);
            }
        }

        Page<Product> products = this.productService.getAllProductWithSpec(pageable,
                productCriteriaDTO);
        // case 1
        // double min = minOptional.isPresent() ? Double.parseDouble(minOptional.get())
        // : 0;
        // Page<Product> products = this.productService.getAllProductWithSpec(pageable,
        // min);
        // case 2
        // double max = maxOptional.isPresent() ? Double.parseDouble(maxOptional.get())
        // : 0;
        // Page<Product> products = this.productService.getAllProductWithSpec(pageable,
        // max);
        // case 3
        // String factory = factoryOptional.isPresent() ? factoryOptional.get() : "";
        // Page<Product> products = this.productService.getAllProductWithSpec(pageable,
        // factory);
        // case 4
        // List<String> factory = Arrays.asList(factoryOptional.get().split(","));
        // Page<Product> products = this.productService.getAllProductWithSpec(pageable,
        // factory);
        // case 5
        // String price = priceOptional.isPresent() ? priceOptional.get() : "";
        // Page<Product> products = this.productService.getAllProductWithSpec(pageable,
        // price);
        // case 6
        // List<String> price = Arrays.asList(priceOptional.get().split(","));
        // Page<Product> products = this.productService.getAllProductWithSpec(pageable,
        // price);

        List<Product> listProducts = products.getContent().size() > 0 ? products.getContent()
                : new ArrayList<Product>();
        String qs = request.getQueryString();
        if (qs != null && !qs.isBlank()) {
            // remove page
            qs = qs.replace("page=" + page, "");
        }
        model.addAttribute("products", listProducts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());// tổng số trang
        model.addAttribute("queryString", qs);
        return "client/product/show";
    }

}
