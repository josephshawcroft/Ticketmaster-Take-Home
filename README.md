# Ticketmaster Take Home Test
Welcome to my technical assignment repository for Ticketmaster!

### Key Features
- MVVM structure
- Jetpack Compose for the UI layer (with some UI tests!)
- Dagger Hilt for DI
- Kotlin flow & coroutines for asynchronous programming
- Room for persistent local storage
- Unit tests

### Structure
#### Data retrieval / repository
- Using Retrofit for the remote API calls- see: `EventApi`. This is then used in `RemoteEventListRepository` in order to retrieve the remote API data, which is exposed as a flow. All backend data is mapped from it's DTO into a domain specific model.
- Using Room for the local data retrieval- see: `EventDao`. This is then used in `LocalEventListRepository` in order to retrieve the local data, which is exposed a flow.

#### Usecases
- `GetEventsForCityUsecase` pulls data from both `LocalEventListRepository` and `RemoteEventListRepository` and combines them into a flow that the consumer can collect. The benefit of doing it like this is scaleability- if, for instance, we wanted to add an additional API call to this usecase, it would be a matter of exposing the new data flow to the usecase and then combining it with the other 2 flows in the `execute` method block.
- The main benefits of having this usecase layer are simplicity and reusability- the VM consumer does not need to worry about the combining of the local and remote data, it's simply given a flow of domain models that it can then present on the screen. It also means that, if we were to flesh out this technical test into a bigger app, this usecase could be used on other screens of the app.

#### ViewModel
- `EventListViewModel` is the 'VM' of the MVVM structure. This VM is pretty straightforward- it has a method to execute the usecase and to collect and process the resulting data. It also has small bit of logic for filtering the list

#### Compose view layer
- `EventListScreen` that contains a `SearchBar` and `EventList`
- `EventList` has a 'pull to refresh' mechanism- useful for when the user has had an error when doing an API call as they can easily refresh without having to restart the app

### Strengths of the app
- Strong separation of concerns, follows the MVVM structure
- Smooth responsive UX- handles orientation changes gracefully
- Robust unit testing of the repository and usecase layers. [Turbine](https://github.com/cashapp/turbine) is a fantastic testing library to use to test flows.
- Inclusion of some UI testing- showed demonstration of knowledge of UI testing when using Compose, specifically in `SearchBarTest`
- [Also included a PR where I fixed a bug I found](https://github.com/josephshawcroft/Ticketmaster-Take-Home/pull/1)

### Areas to improve
- Logic of usecases combine function could be simplified- ran out of time to improve this. Could expose the room DAO as a flow and observe that, then just insert any API updates to Room DB and continue to read from there. Also need to clear the previous events as they may no longer be valid
- The repository and usecase layers handle the data retrieval in a solid manner and provide quite a comprehensive level of detail to the view model as to what the data is and where the data has come from. However, the view model and view layers do not do a comprehensive enough handling of these data states. For instance, if a local db call succeeds but the remote API call fails, the VM logic/UI logic does not yet accurately cover this scenario- ideally we should show an 'offline', 'cached data' view to the user. As the repo/usecase layer is robust, it's simply a matter of me putting more time in to make the VM/UI cover all bases.
- Although I've been thorough in my unit testing of the local and remote repositories and usecase classes, I've ran out of time to implement tests at the VM layer (and also other classes such as the `EventApi` class and `EventMapper` class). This should be fairly straightforward as I've been considerate of testability when making these classes (eg dependencies injected via DI to be easy to mock during testing).
- I've added a comprehensive level of UI testing for the `SearchBar` for demonstration purposes (as well as a simple test for `EventItem`). Given more time I'd extend this to a full UI test of the `EventListScreen`.
- Better support for larger devices- while the app works fine on all screen sizes, I've not yet done enough to accomodate for larger devices such as tablets. For example, adding an additional variant of the `EventItem` for medium and expanded window sizes where the image thumbnail is larger and the items don't take up the full width of the screen (or maybe even a grid view on tablet?).
- Add in pagination so I can display more than 20 items, removing the hardcoded city value when doing the API calls,
  
### Screenshots
#### Phone
![image](https://github.com/josephshawcroft/Ticketmaster-Take-Home/assets/8886237/7f2a43dd-d1a3-4a8b-b16a-723acd5e7de8)
![image](https://github.com/josephshawcroft/Ticketmaster-Take-Home/assets/8886237/ec7a04bd-9220-4463-a825-cc8c687494e7)

#### Tablet
![image](https://github.com/josephshawcroft/Ticketmaster-Take-Home/assets/8886237/7a7fca33-1fb4-484c-b7a2-c9ef5dbc4e48)
![image](https://github.com/josephshawcroft/Ticketmaster-Take-Home/assets/8886237/1c8db498-aa6b-4dec-88e1-0913a909cfe6)



