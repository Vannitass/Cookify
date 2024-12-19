import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pageforregister.MainPage.Card
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


        Picasso.get()
            .load(recipe.image_path) // Используем полный URL из поля image_path
            .placeholder(R.drawable.placeholder_image) // Плейсхолдер во время загрузки
            .into(holder.image)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, Card::class.java)

            intent.putExtra("title", recipe.title)
            intent.putExtra("description", recipe.description)
            intent.putExtra("author", recipe.author)
            intent.putExtra("imagePath", recipe.image_path)

            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = recipes.size

    // Метод для обновления списка
    fun updateRecipes(newRecipes: List<Recipe>) {
        recipes.clear()
        recipes.addAll(newRecipes)
        notifyDataSetChanged()
    }
}
