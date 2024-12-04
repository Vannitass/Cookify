from flask import Flask, jsonify, request
import sqlite3
import fun

app = Flask(__name__)


@app.route('/greet', methods=['GET'])
def greet():
    naznach = request.args.get('purpose')
    login = request.args.get('login')
    email = request.args.get('email')
    password = request.args.get('password')

    if naznach == "Registr":
        result = fun.register_user(login, email, password)
        if result:
            return jsonify({"status": "success", "message": f"Пользователь {login} зарегистрирован"})
        else:
            return jsonify({"status": "error", "message": "Пользователь уже существует или ошибка базы данных"}), 400

    elif naznach == "Auth":
        result = fun.authenticate_user(login, password)
        if result:
            return jsonify(result)
        else:
            return jsonify({"status": "error", "message": "Неверные учетные данные"}), 401

    return jsonify({"status": "error", "message": "Неверное значение purpose"}), 400


@app.route('/upload_recipe', methods=['POST'])
def upload_recipe():
    if 'image' not in request.files:
        return jsonify({"status": "error", "message": "Изображение не загружено"}), 400

    image = request.files['image']
    title = request.form.get('title')
    description = request.form.get('description')
    content = request.form.get('content')
    author = request.form.get('author')

    if not (title and description and content and author):
        return jsonify({"status": "error", "message": "Все поля должны быть заполнены"}), 400

    image_path = f'uploads/{image.filename}'
    image.save(image_path)

    result = fun.add_recipe(title, description, image_path, content, author)
    if result:
        return jsonify({"status": "success", "message": "Рецепт загружен"}), 200
    else:
        return jsonify({"status": "error", "message": "Ошибка базы данных"}), 500

@app.route('/get_recipes', methods=['GET'])
def get_recipes():
    recipes = fun.get_all_recipes()
    return jsonify({"status": "success", "recipes": recipes}), 200


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
