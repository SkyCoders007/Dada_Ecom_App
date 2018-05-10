package com.mxi.ecommerce.comman;


public class ConnectionUrl {

    public static String baseUrl = "http://mbdbtechnology.com/projects/1811/index.php?route=api/account/";

    public static String signup = baseUrl + "register";
    public static String login = baseUrl + "login";
    public static String forgotpassword = baseUrl + "forgotten";
    public static String logout = baseUrl + "logout";
    public static String getcustomerbyemail = baseUrl + "login/" + "getcustomerbyemail";
    public static String socialLogin = baseUrl + "login/" + "sociallogin";
    public static String homelisting = "http://mbdbtechnology.com/projects/1811/index.php?route=api/common/home";
    public static String categorylisting = "http://mbdbtechnology.com/projects/1811/index.php?route=api/product/category";
    public static String Productlisting = "http://mbdbtechnology.com/projects/1811/index.php?route=api/product/product";
    public static String CategoryListing = "http://mbdbtechnology.com/projects/1811/index.php?route=api/product/category/filter";
    public static String FilterPramaters = "http://mbdbtechnology.com/projects/1811/index.php?route=api/product/product/getFilters";
    public static String FilterData = "http://mbdbtechnology.com/projects/1811/index.php?route=api/product/product/FilterData";
    public static String CartCounter = "http://mbdbtechnology.com/projects/1811/index.php?route=api/checkout/cart/carttotal";

    /*Add address*/

    public static String edit_profile = "http://mbdbtechnology.com/projects/1811/index.php?route=api/account/edit";
    public static String get_county_name = "http://mbdbtechnology.com/projects/1811/index.php?route=api/account/address/country";
    public static String get_state_name = "http://mbdbtechnology.com/projects/1811/index.php?route=api/account/address/get_zone_by_country";
    public static String add_address = "http://mbdbtechnology.com/projects/1811/index.php?route=api/account/address/add";
    public static String get_address = "http://mbdbtechnology.com/projects/1811/index.php?route=api/account/address";
    public static String edit_address = "http://mbdbtechnology.com/projects/1811/index.php?route=api/account/address/edit";
    public static String get_address_delete = "http://mbdbtechnology.com/projects/1811/index.php?route=api/account/address/delete";


    /*SearchActivity*/
    public static String searchUrl= "http://mbdbtechnology.com/projects/1811/index.php?route=api/product/search";
    public static String searchUrlEng= "http://mbdbtechnology.com/projects/1811/index.php?route=api/product/search&language=en-gb&search=";
    public static String searchUrlArabi= "http://mbdbtechnology.com/projects/1811/index.php?route=api/product/search&language=arabi&search=";


    /*Change Password Activity*/
    public static String change_passrord = "http://mbdbtechnology.com/projects/1811/index.php?route=api/account/password";


    /*Add wishlist*/
    public static String add_wishlist = "http://mbdbtechnology.com/projects/1811/index.php?route=api/account/wishlist/add";
    public static String get_wishlist = "http://mbdbtechnology.com/projects/1811/index.php?route=api/account/wishlist";

    /*Order History*/
    public static String order_history = "http://mbdbtechnology.com/projects/1811/index.php?route=api/account/order";
    public static String order_get_history = "http://mbdbtechnology.com/projects/1811/index.php?route=api/account/order/info";

    /*Edit Profile*/
    public static String get_edit_profile = "http://mbdbtechnology.com/projects/1811/index.php?route=api/account/login/getcustomerbytoken";

    /*Send FeedBack*/
    public static String send_feedback = "http://mbdbtechnology.com/projects/1811/index.php?route=api/common/feedback";

    /*Order Tracking*/

    public static String order_tracking = "http://mbdbtechnology.com/projects/1811/index.php?route=api/account/order/product_order_tracking";

    /*Topdeal data*/

    public static String get_topdeal = "http://mbdbtechnology.com/projects/1811/?route=api/product/category/topdeal";


    /*Add to cart*/

    public static String add_to_cart = "http://mbdbtechnology.com/projects/1811/index.php?route=api/checkout/cart/add";


    /*Cart Detail*/

    public static String cart_detail = "http://mbdbtechnology.com/projects/1811/index.php?route=api/checkout/cart";
    public static String remove_cart = "http://mbdbtechnology.com/projects/1811/index.php?route=api/checkout/cart/remove";

    /*Cart Update*/
    public static String cart_update = "http://mbdbtechnology.com/projects/1811/index.php?route=api/checkout/cart/edit";

    /*Payment Detail*/

    public static String payment_method = "http://mbdbtechnology.com/projects/1811/index.php?route=api/checkout/checkout/payment_methods";


    /*Checkout*/

    public static String checkout = "http://mbdbtechnology.com/projects/1811/index.php?route=api/checkout/checkout";

    /*Today Deal */

    public static String todaytopdeal = "http://mbdbtechnology.com/projects/1811/?route=api/product/category/todaytopdeal";

    /*returnorder */

    public static String returnorder = "http://mbdbtechnology.com/projects/1811/index.php?route=api/account/return";

    /*returnorder list */

    public static String returnorder_reason = "http://mbdbtechnology.com/projects/1811/index.php?route=api/account/return/return_reson";

    /*returnorder reason submit */

    public static String returnorder_reason_submit = "http://mbdbtechnology.com/projects/1811/index.php?route=api/account/return/add";

}

