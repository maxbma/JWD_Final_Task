package by.tc.library.bean;

import java.util.List;

/**Класс книги со свойствами: id, bookName, authorSurname,
 * authorName, description, releaseYear, ordersAmount, picture и genres*/
public class Book {
	/**Поле id книги*/
	private int id;
	/**Поле название книги*/
	private String bookName;
	/**Поле фамилия автора*/
	private String authorSurname;
	/**Поле имя автора*/
	private String authorName;
	/**Поле описание книги*/
	private String description;
	/**Поле год издания*/
	private int releaseYear;
	/**Поле количество заказов*/
	private int ordersAmount;
	/**Поле картинка книги*/
	private String picture;
	/**Поле жанры книги*/
	private List<String> genres;


	/**Конструктор - создание нового объекта с определенными значениями
	 * @param id - id книги
	 * @param bookName - название книги
	 * @param authorName - имя автора
	 * @param authorSurname - фамилия автора
	 * @param releaseYear - год издания
	 * @param description - описание книги
	 * @param picture - имя файла картинки книги
	 * @see Book#Book(int, String, String, String, int, String) */
	public Book(int id, String bookName, String authorSurname, String authorName, int releaseYear,
				String description, String picture) {
		super();
		this.id = id;
		this.bookName = bookName;
		this.authorSurname = authorSurname;
		this.authorName = authorName;
		this.releaseYear = releaseYear;
		this.description = description;
		this.picture = picture;
	}

	/**Конструктор - создание нового объекта с определенными значениями
	 * @param id - id книги
	 * @param bookName - название книги
	 * @param authorName - имя автора
	 * @param authorSurname - фамилия автора
	 * @param releaseYear - год издания
	 * @param description - описание книги
	 * @see Book#Book(int, String, String, String, int, String, String) */
	public Book(int id, String bookName, String authorSurname, String authorName, int releaseYear,
				String description) {
		super();
		this.id = id;
		this.bookName = bookName;
		this.authorSurname = authorSurname;
		this.authorName = authorName;
		this.releaseYear = releaseYear;
		this.description = description;
	}

	/**Переопределение функции equals
	 * @return возвращает true, если две книги имеют одинаковые поля:
	 * {@link Book#bookName},
	 * {@link Book#authorName},
	 * {@link Book#authorSurname},
	 * {@link Book#releaseYear}*/
	@Override
	public boolean equals(Object obj){
		if(null == obj || obj.getClass()!=this.getClass()){
			return false;
		}
		if(this == obj){
			return true;
		}
		Book book = (Book)obj;
		return this.bookName.equals(book.getBookName()) &&
				this.authorName.equals(book.getAuthorName()) &&
				this.authorSurname.equals(book.getAuthorSurname()) &&
				this.releaseYear == book.getReleaseYear();
	}

	/**Переопределение функции hashCode
	 * @return возвращает hashCode объекта*/
	@Override
	public int hashCode(){
		return 29*id + bookName.hashCode() - 31*releaseYear;
	}

	/**Функция получает значение поля {@link Book#genres}
	 * @return возвращает список жанров книги */
	public List<String> getGenres() {
		return genres;
	}

	/**Процедура определения жанров {@link Book#genres}
	 * @param genres - список жанров*/
	public void setGenres(List<String> genres) {
		this.genres = genres;
	}

	/**Функция получает значение поля {@link Book#ordersAmount}
	 * @return - возвращает количество заказов книги*/
	public int getOrdersAmount() {
		return ordersAmount;
	}

	/**Процедура определения значения поля {@link Book#ordersAmount}
	 * @param ordersAmount - количество заказов*/
	public void setOrdersAmount(int ordersAmount) {
		this.ordersAmount = ordersAmount;
	}

	/**Функция получения значения поля {@link Book#id}
	 * @return возвращает id книги*/
	public int getId() {
		return id;
	}
	/**Функция получения значения поля {@link Book#bookName}
	 * @return возвращает название книги*/
	public String getBookName() {
		return bookName;
	}
	/**Функция получения значения поля {@link Book#authorSurname}
	 * @return возвращает фамилию автора*/
	public String getAuthorSurname() {
		return authorSurname;
	}
	/**Функция получения значения поля {@link Book#authorName}
	 * @return возвращает имя автора*/
	public String getAuthorName() {
		return authorName;
	}
	/**Функция получения значения поля {@link Book#releaseYear}
	 * @return возвращает год издания*/
	public int getReleaseYear() {
		return releaseYear;
	}
	/**Функция получения значения поля {@link Book#description}
	 * @return возвращает описание книги*/
	public String getDescription(){
		return description;
	}
	/**Функция получения значения поля {@link Book#picture}
	 * @return возвращает название файла с картинкой книги*/
	public String getPicture(){
		return picture;
	}
	/**Процедура определеня значения поля {@link Book#picture}
	 * @param picture - название файла с картинкой книги*/
	public void setPicture(String picture){
		this.picture = picture;
	}
}
