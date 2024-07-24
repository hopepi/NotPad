package com.example.notpad.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notpad.model.Note
import com.example.notpad.service.NoteDatabase
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(application: Application) : BaseViewModel(application) {

    var notes = mutableStateOf<List<Note>>(emptyList())
    init {
        loadNotesSQLite()
    }
    private var initialNote = listOf<Note>()
    private var isSearchStarting = true


    fun searchNote(query: String){
        val listToSearch = if(isSearchStarting){
            notes.value
        }else{
            initialNote
        }

        viewModelScope.launch (Dispatchers.Default){
            if (query.isEmpty()){
                notes.value = initialNote
                isSearchStarting = true
                return@launch
            }
            val result = listToSearch.filter {
                it.noteHead!!.contains(query.trim(),ignoreCase = true)
            }

            if (isSearchStarting){
                initialNote = notes.value
                isSearchStarting = false
            }

            notes.value=result

        }

    }


    /*
    ------------------------------------------------Dikkat------------------------------------------------
    Eğer launch ifadesini tek başına kullanırsak hata ile karşılaşabiliriz yani ne demeye çalışıyorum
    hilt launch{} ifadesinde hata vermezken bunu Dispatcher.IO olarak belirtince hata almaya başlıyoruz
    peki neden hilt viewmodelde maalesef buna direk launch ifadesiyle erişmemize olanak tanımıyor
    peki ne yapmalıyız veri tabanı gibi işlemlerde yani IO threadini kullanıcak yerlerde viewmodel.launch yapıp
    Dispatchers.IO olduğunu belirtmemiz gerek hilt böyle yaptığımızda kararımıza saygı duyacaktır ve IO
    kullanımımız doğru olacaktır

    Hata sebepleri ve açıklamaları

    1)Hilt ve ViewModel: Hilt, Dependency Injection (Bağımlılık Enjeksiyonu) sağlayan bir kütüphanedir ve
    ViewModel'leri yönetmek için @HiltViewModel kullanır. Bu durumda, ViewModel içinde launch kullanırken doğrudan
    Dispatchers.IO gibi kapsamları belirtmek hatalara yol açabilir.

    2)ViewModelScope Kullanımı: viewModelScope kavramı,
    ViewModel yaşam döngüsüne bağlı olarak işlemleri yönetir ve
    otomatik olarak iptal edilmesini sağlar. Bu şekilde, asenkron işlemler
    ViewModel tarafından tutulan verilere güvenli bir şekilde erişebilir ve hata yönetimi daha düzenli olur.

    3)IO Thread Kullanımı: Veritabanı gibi işlemlerde IO threadini kullanmak önemlidir.
    ViewModel içinde viewModelScope.launch(Dispatchers.IO)
    kullanarak IO işlemlerini güvenli bir şekilde yönetmek en uygun yaklaşımdır.


    --
    loadNotesSQLite fonksiyonu, Room veritabanı işlemlerini arka planda (IO thread'inde) gerçekleştiriyor.
    Bu, Room'un veritabanı işlemlerinin uzun süren işlemler için uygun olan bir thread üzerinde yapılacağını belirtir.
    */
    fun loadNotesSQLite(){
        viewModelScope.launch(Dispatchers.IO) {
            val dao = NoteDatabase(getApplication()).noteDao()
            val notesList = dao.getAllNotes()
            /*
            ------------------------------------------------Dikkat------------------------------------------------
            getAllNotes işlemi tamamlandığında, UI thread üzerinde (Dispatchers.Main) notları güncelliyoruz.
            Bu, Room veritabanı işlemlerinin tamamlanmasını bekler ve UI thread üzerinde güvenli bir şekilde
            UI'nin güncellenmesini sağlar. Bu, UI performansını etkilemeden veritabanı işlemlerinin sonucunu
            doğru şekilde görüntülememizi sağlar.
             */

            viewModelScope.launch(Dispatchers.Main) {
                /*
                Veritabanından notları aldıktan sonra, bu notları UI thread üzerinde notes değerine atıyoruz.
                Bu şekilde, Compose'un UI thread üzerinde notları güncellemesini sağlıyoruz.
                */
                notes.value = notesList
            }
        }
    }
}