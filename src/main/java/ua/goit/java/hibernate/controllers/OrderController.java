package ua.goit.java.hibernate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ua.goit.java.hibernate.dao.DishDao;
import ua.goit.java.hibernate.dao.EmployeeDao;
import ua.goit.java.hibernate.dao.OrderDao;
import ua.goit.java.hibernate.model.Dish;
import ua.goit.java.hibernate.model.DishCategory;
import ua.goit.java.hibernate.model.Measures;
import ua.goit.java.hibernate.model.Orders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderController {
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private DishDao dishDao;
    @Autowired
    private OrderDao orderDao;

    public Orders createOrder(String waiterName, List<String> dishes, int tableNumber){

        Orders order = new Orders();
        order.setWaiter(employeeDao.findByName(waiterName));
        order.setDishes(createDishes(dishes));
        order.setTableNumber(tableNumber);
        order.setOrderDate(new Date());

        return order;
    }
    @Transactional
    public void initOrders(){

        List<String> dishesFirst = new ArrayList<>();
        dishesFirst.add("Plov");
        dishesFirst.add("Cake");

        orderDao.save(createOrder("Peter", dishesFirst, 27));

        List<String> dishesSecond = new ArrayList<>();
        dishesSecond.add("Milk");
        dishesSecond.add("Plov");

        orderDao.save(createOrder("Ivan", dishesSecond, 5));


        orderDao.save(testCreateDishWithIceCream());  // use CASCADE to save new dish in new order
    }
    @Transactional
    public void addNewOrder(Orders order) {
            orderDao.save(order);

    }

    @Transactional
    public void removeOrder(Orders order){
        orderDao.deleteOrder(order);
    }

    @Transactional
    public void addDishToOrder(Dish dish, Orders order) {
        orderDao.addDishToOrder(dish, order);
    }
    @Transactional
    public void deleteDishFromOrder(Dish dish, Orders orders) {
        orderDao.deleteDishFromOrder(dish, orders);
    }
    @Transactional
    public Orders findById(Long id) {
        return orderDao.findById(id);
    }

    @Transactional
    public void closeOrder(Orders order) {
        orderDao.closeOrder(order);
    }
    @Transactional
    public List<Orders> findAllOrders() {
        return orderDao.findAllOrders();
    }
    @Transactional
    public List<Orders> findOpenedOrders() {
        return orderDao.findAllOpenedOrders();
    }
    @Transactional
    public List<Orders> findClosedOrders() {
        return orderDao.findAllClosedOrders();
    }

    public Orders testCreateDishWithIceCream() {  // при помощи CASCADE нет необходимости сначала записывать новое блюдо в БД,
                                                // а потом уже создавать новый ордер
        List<Dish> iceDishes = new ArrayList<>();
        Dish iceCream = new Dish();
        iceCream.setName("Ice Cream");
        iceCream.setCategory(DishCategory.DESSERT);
        iceCream.setPrice(3.0F);
        iceCream.setWeight(100.0F);
        iceCream.setMeasure(Measures.KG);
        iceDishes.add(iceCream);

        Orders order = new Orders();
        order.setWaiter(employeeDao.findByName("Ivan"));
        order.setDishes(iceDishes);
        order.setTableNumber(99);
        order.setOrderDate(new Date());


        return order;
    }









@Transactional
    public List<Orders> getAllOrders(){
        return orderDao.findAllOrders();
    }

@Transactional
    public void printAllOrders() {
        getAllOrders().forEach(System.out::println);

    }
    @Transactional
    public void removeAllOrders() {
        orderDao.removeAllOrders();
    }

    private List<Dish> createDishes(List<String> dishes) {

        List<Dish> result = new ArrayList<>();
        for (String dishName : dishes) {
            result.add(dishDao.findByName(dishName));
        }

        return result;
    }

    public void setEmployeeDao(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    public void setDishDao(DishDao dishDao) {
        this.dishDao = dishDao;
    }

    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }
}
