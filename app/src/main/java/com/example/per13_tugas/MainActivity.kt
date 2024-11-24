package com.example.per13_tugas

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.per13_tugas.database.Pemilih
import com.example.per13_tugas.database.PemilihDatabase
import com.example.per13_tugas.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var pemilihDatabase: PemilihDatabase
    private lateinit var pemilihAdapter: PemilihAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pemilihDatabase = PemilihDatabase.getDatabase(this)
        pemilihAdapter = PemilihAdapter(
            onEditClick = { pemilih -> editPemilih(pemilih) },
            onDeleteClick = { pemilih -> deletePemilih(pemilih) },
            onShowClick = { pemilih -> showPemilih(pemilih) }
        )

        binding.rcDataPemilih.layoutManager = LinearLayoutManager(this)
        binding.rcDataPemilih.adapter = pemilihAdapter

        binding.btnTambahData.setOnClickListener {
            startActivity(Intent(this, TambahActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            SharedPreferencesHelper(this).logout()
            finish()
        }

        loadPemilihData()
    }

    private fun loadPemilihData() {
        lifecycleScope.launch {
            val pemilihList = pemilihDatabase.pemilihDao().getAllPemilih()
            pemilihAdapter.submitList(pemilihList)
        }
    }

    private fun editPemilih(pemilih: Pemilih) {
        // Implementasi untuk mengedit pemilih
        val intent = Intent(this, EditActivity::class.java).apply {
            putExtra("pemilih_id", pemilih.id)
        }
        startActivity(intent)
    }

    private fun deletePemilih(pemilih: Pemilih) {
        // Implementasi untuk menghapus pemilih
        lifecycleScope.launch {
            pemilihDatabase.pemilihDao().delete(pemilih)
            loadPemilihData() // Refresh data setelah penghapusan
        }
    }

    private fun showPemilih(pemilih: Pemilih) {
        // Implementasi untuk menampilkan detail pemilih
        val intent = Intent(this, ShowActivity::class.java).apply {
            putExtra("pemilih_id", pemilih.id)
        }
        startActivity(intent)
    }
}