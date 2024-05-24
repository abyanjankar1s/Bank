# Bank
Object Oriented Programming: Designing Banking System. 


We'll create a base Transaction class, which will be extended by Deposit, Withdrawal, and OpenAccount classes.
A BankTeller will be defined by a unique ID, while customers will be represented through the BankAccount class, which includes their ID and balance.
The central bank, or headquarters, will consist of multiple BankBranch objects, and there will be a single BankSystem to centrally manage customer accounts and transactions.
Customers may conduct transactions at various branches, so their information must be centrally stored within the BankSystem.

#DEMO


    class Main {
    public static void main(String[] args){
        BankSystem bankSystem = new BankSystem(new ArrayList<BankAccount>(), new ArrayList<Transaction>());
        Bank bank = new Bank(new ArrayList<BankBranch>(), bankSystem, 10000);

        BankBranch branch1 = bank.addBranch("420 spanish street", 1000);
        BankBranch branch2 = bank.addBranch("2020 dallas mavs", 1000);

        branch1.addTeller(new BankTeller(1));
        branch2.addTeller(new BankTeller(2));

        int personId1 = branch1.OpenAccount("Luka Doncic");
        int personId2 = branch2.OpenAccount("Kyrie Irving");

        branch1.deposit(personId1, 10000);
        branch2.deposit(personId2, 20000);

        branch1.withdraw(personId1, 30);

        bank.printTransaction();

        Teller 1 opened account 0
    //Teller 2 opened account 1
    //Teller 1 deposited 10000 to account number 0
    //Teller 2 deposited 20000 to account number 1
    //Teller 1 withdrew 30 from account number 0


    }
    }
