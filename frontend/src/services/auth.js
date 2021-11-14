class AuthService {

    login(json) {
        localStorage.setItem("user", JSON.stringify(json));
    }

    getCurrentUser(){
        return JSON.parse(localStorage.getItem('user'));
    }

    logout() {
        localStorage.removeItem("user");
    }
}

export default new AuthService();