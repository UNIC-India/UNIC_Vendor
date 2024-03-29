package com.unic.unic_vendor_final_1.datamodels;

        import com.google.firebase.firestore.DocumentId;
        import com.google.firebase.firestore.ServerTimestamp;

        import java.util.Comparator;
        import java.util.Date;
        import java.util.List;
        import java.util.Map;

public class Order {

    @DocumentId
    private String id;
    private String ownerId, shopId;
    private double total;
    private List<Map<String,Object>> items;
    private List<Integer> quantity;
    private int no_of_items;
    private int orderStatus;
    int pickUp=0;
    com.unic.unic_vendor_final_1.datamodels.Address orderAddress;
    @ServerTimestamp
    private
    Date time;
    @ServerTimestamp
    private
    Date updateTime;
    private String phoneNo;
    private String instructions;
    private String GSTIN;
    private boolean reported;


    public Order(){
        reported = false;
    }
    

    public Order(String shopId, List<Map<String, Object>> items, List<Integer> quantity, int no_of_items) {
        this.shopId = shopId;
        this.items = items;
        this.quantity = quantity;
        this.no_of_items = no_of_items;

        this.id=" ";
        this.orderStatus=0;

        this.total=0;
        this.instructions=" ";
        this.GSTIN=" ";
        this.reported = false;
    }
    public static Comparator<Order> compareByDate=new Comparator<Order>() {
        @Override
        public int compare(Order o1, Order o2) {
            return o2.getTime().compareTo(o1.getTime());
        }
    };


        public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }



    public List<Map<String, Object>> getItems() {
        return items;
    }

    public void setItems(List<Map<String, Object>> items) {
        this.items = items;
    }

    public List<Integer> getQuantity() {
        return quantity;
    }

    public void setQuantity(List<Integer> quantity) {
        this.quantity = quantity;
    }

    public int getNo_of_items() {
        return no_of_items;
    }

    public void setNo_of_items(int no_of_items) {
        this.no_of_items = no_of_items;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }


    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public int getPickUp() {
        return pickUp;
    }

    public void setPickUp(int pickUp) {
        this.pickUp = pickUp;
    }

    public Address getAddress() {
        return orderAddress;
    }

    public void setAddress(Address orderAddress) {
        this.orderAddress = orderAddress;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getGSTIN() {
        return GSTIN;
    }

    public void setGSTIN(String GSTIN) {
        this.GSTIN = GSTIN;
    }

    public boolean isReported() {
        return reported;
    }

    public void setReported(boolean reported) {
        this.reported = reported;
    }
}