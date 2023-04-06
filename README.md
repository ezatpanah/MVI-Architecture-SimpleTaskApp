## SimpleTaskApp using MVI Architecture 

<img width="400" src="https://user-images.githubusercontent.com/94545831/230331223-d3c9a182-1454-4618-b2bb-6da1513e61e6.png" />

- User does an action which will be an Intent → Intent is a state which is an input to model → Model stores state and send the requested state to the 
View → View Loads the state from Model → Displays to the user. 
- If we observe, the data will always flow from the user and end with the user through intent. 
- It cannot be the other way, Hence its called **Unidirectional architecture**. If the user does one more action the same cycle is repeated, hence it is Cyclic.


### Overview 
- The purpose of this series is to teach different architectures with different structures, and you can check these architectures and structures in the same App.
- Coming soon on [AndroidGeek](https://www.youtube.com/c/AndroidGeekco) Channel

### This project is in
- [MVP architecture](https://github.com/ezatpanah/SimpleTaskApp-MVP) [Room Database - RxJava - Dagger Hilt - Lottie - View Binding]
- [MVVM architecture](https://github.com/ezatpanah/SimpleTaskApp-MVVM) [Room Database - LiveData - Flow - Coroutines - Dagger Hilt - ViewModel - Lottie - View Binding]
- MVI architecture : Current Repo 


### Technologies and Libraries Used 

- Room Database
- Flow
- StateFlow
- Dagger Hilt
- Coroutines
- Lottie
- View Binding


