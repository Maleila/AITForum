package hu.ait.aitforum.ui.screen.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginScreenViewModel : ViewModel() {

    var loginUiState: LoginUiState by mutableStateOf(LoginUiState.Init)

    private lateinit var auth: FirebaseAuth

    init {
        auth = Firebase.auth
    }

    fun registerUser(email: String, password: String) {
        loginUiState = LoginUiState.Loading

        try{
            auth.createUserWithEmailAndPassword(email, password).
                    addOnSuccessListener {
                        loginUiState = LoginUiState.RegisterSuccess
                    }
                .addOnFailureListener{
                    LoginUiState.Error(it.message)
                }
        } catch(e: Exception) {
            loginUiState = LoginUiState.Error(e.message)
        }
    }

    fun loginUser(email: String, password: String) {
        loginUiState = LoginUiState.Loading
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener{
                    loginUiState = LoginUiState.LoginSuccess
                }
                .addOnFailureListener{
                    loginUiState = LoginUiState.Error(it.message)
                }
        } catch (e: Exception) {
            loginUiState = LoginUiState.Error(e.message)
        }
    }

}

sealed interface LoginUiState {
    object Init : LoginUiState
    object Loading : LoginUiState
    object LoginSuccess : LoginUiState
    object RegisterSuccess : LoginUiState
    data class Error(val error: String?) : LoginUiState
}