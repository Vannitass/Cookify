import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pageforregister.MainPage.Recipe
import com.example.pageforregister.R
import com.squareup.picasso.Picasso

class RecipeAdapter(private val recipes: MutableList<Recipe>) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.cardTitle) // Используем id из item_in_list.xml
        val description: TextView = itemView.findViewById(R.id.cardDescription) // id для описания
        val author: TextView = itemView.findViewById(R.id.userNickname) // id для автора
        val image: ImageView = itemView.findViewById(R.id.cardImage) // id для изображения
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_in_list, parent, false) // Используем ваш XML
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.title.text = recipe.title
        holder.description.text = recipe.description
        holder.author.text = recipe.author

//        // Загрузка изображения с помощью Picasso вариант 1
//        Picasso.get()
//            .load("http://<IP>:5000/${recipe.image_path}") // Замените <IP> на IP вашего сервера
//            .placeholder(R.drawable.placeholder_image) // Плейсхолдер во время загрузки
//            //.error(R.drawable.error_image) // Изображение при ошибке загрузки
//            .into(holder.image)

        // Загрузка изображения с помощью Picasso  вариант 2
        Picasso.get().load(recipe.image_path).into(holder.image)
    }

    override fun getItemCount(): Int = recipes.size

    // Метод для обновления списка
    fun updateRecipes(newRecipes: List<Recipe>) {
        recipes.clear()
        recipes.addAll(newRecipes)
        notifyDataSetChanged()
    }
}
