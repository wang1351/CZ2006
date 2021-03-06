import React from "react";
import ProfilePage from "./Boundary/ProfilePage";
import HomePage from "./Boundary/HomePage";
import "./App.css";
// import {CSSTransition, TransitionGroup} from "react-transition-group";

import {
    BrowserRouter as Router,
    Switch,
    Route, Redirect
} from "react-router-dom";
import LoginPage from "./Boundary/LoginPage";
import PrimaryCriteriaMatching from "./Boundary/PrimaryCriteriaMatching";
import SecondaryCriteriaMatching from "./Boundary/SecondaryCriteriaMatching";
import UploadHousePage from "./Boundary/UploadHousePage";
import AreaListUI from "./Boundary/AreaListUI";
import HouseList from "./Boundary/HouseList";

class App extends React.Component {
    constructor() {
        super();

        this.state = {
            loggedInStatus: sessionStorage.getItem("loggedInStatus"),
            email: sessionStorage.getItem("email"),
            uuid: sessionStorage.getItem("uuid")
        };

        this.handleLogin = this.handleLogin.bind(this);
        this.handleLogout = this.handleLogout.bind(this);
    }

    handleLogout() {
        this.setState({
            loggedInStatus: "NOT_LOGGED_IN",
            email: null
        });
        sessionStorage.clear()
    }

    handleLogin(email, uuid) {
        this.setState({
            loggedInStatus: "LOGGED_IN",
            email: email
        });

        sessionStorage.setItem("loggedInStatus", "LOGGED_IN");
        sessionStorage.setItem("email", email);
        sessionStorage.setItem("uuid", uuid);
    }

    render() {
        const loginStatus = this.state.loggedInStatus;
        return (
            <Router>
                <div>
                        {
                            loginStatus ?
                                <Switch>
                                    <Route path={"/"} exact component={HomePage}/>
                                    <Route exact path="/login"
                                           render={props => (
                                               <LoginPage
                                                   {...props}
                                                   handleLogin={this.handleLogin}
                                                   loggedInStatus={loginStatus}
                                               />
                                           )}/>
                                    <Route path="/profile"
                                           render={
                                               props => (
                                                   <ProfilePage
                                                       {...props}
                                                       handleLogout={this.handleLogout}
                                                   />
                                               )
                                           }/>
                                    <Route path="/criteria" exact component={PrimaryCriteriaMatching}/>
                                    <Route path="/criteria_" exact component={SecondaryCriteriaMatching}
                                    />
                                    <Route path="/arealist" exact component={AreaListUI}/>
                                    <Route path="/house@area" exact component={HouseList}/>
                                    <Route path="/upload" exact component={UploadHousePage}/>
                                    <Redirect from="/*" to="/"/>
                                </Switch>
                                :
                                <Switch>
                                    <Route path={"/"} exact component={HomePage}/>
                                    <Route exact path="/login"
                                           render={props => (
                                               <LoginPage
                                                   {...props}
                                                   handleLogin={this.handleLogin}
                                                   loggedInStatus={loginStatus}
                                               />
                                           )}/>
                                    <Redirect from="/*" to="/login"></Redirect>
                                </Switch>
                        }
                </div>
            </Router>
        );
    }
}

export default App;
