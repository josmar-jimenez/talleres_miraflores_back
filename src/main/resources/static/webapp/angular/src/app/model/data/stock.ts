export class Stock {
    id: number | null;
    statusId: number;
    storeId: number;  
    productId: number;  
    stock: number; 

    constructor(id: number| null, statusId: number, storeId: number, productId: number, stock: number) {
        this.id = id;
        this.statusId = statusId;
        this.storeId = storeId; 
        this.productId = productId;
        this.stock = stock; 
    }
} 