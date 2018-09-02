package com.example.ccele.project_cocktails.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ccele on 8/4/2018.
 */

public class Cocktail implements Parcelable {

    @SerializedName("idDrink")
    private int id;

    @SerializedName("strDrink")
    private String name;

    @SerializedName("strGlass")
    private String glass;

    @SerializedName("strInstructions")
    private String instructions;

    @SerializedName("strDrinkThumb")
    private String imageUrl;

    @SerializedName("strIngredient1")
    private String ingredient1;

    @SerializedName("strMeasure1")
    private String measure1;

    @SerializedName("strIngredient2")
    private String ingredient2;

    @SerializedName("strMeasure2")
    private String measure2;

    @SerializedName("strIngredient3")
    private String ingredient3;

    @SerializedName("strMeasure3")
    private String measure3;

    @SerializedName("strIngredient4")
    private String ingredient4;

    @SerializedName("strMeasure4")
    private String measure4;

    @SerializedName("strIngredient5")
    private String ingredient5;

    @SerializedName("strMeasure5")
    private String measure5;

    @SerializedName("strIngredient6")
    private String ingredient6;

    @SerializedName("strMeasure6")
    private String measure6;

    private boolean isFavorite;

    public Cocktail(){ }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGlass() {
        return glass;
    }

    public void setGlass(String glass) {
        this.glass = glass;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getIngredient1() {
        return ingredient1;
    }

    public void setIngredient1(String ingredient1) {
        this.ingredient1 = ingredient1;
    }

    public String getMeasure1() {
        return measure1;
    }

    public void setMeasure1(String measure1) {
        this.measure1 = measure1;
    }

    public String getIngredient2() {
        return ingredient2;
    }

    public void setIngredient2(String ingredient2) {
        this.ingredient2 = ingredient2;
    }

    public String getMeasure2() {
        return measure2;
    }

    public void setMeasure2(String measure2) {
        this.measure2 = measure2;
    }

    public String getIngredient3() {
        return ingredient3;
    }

    public void setIngredient3(String ingredient3) {
        this.ingredient3 = ingredient3;
    }

    public String getMeasure3() {
        return measure3;
    }

    public void setMeasure3(String measure3) {
        this.measure3 = measure3;
    }

    public String getIngredient4() {
        return ingredient4;
    }

    public void setIngredient4(String ingredient4) {
        this.ingredient4 = ingredient4;
    }

    public String getMeasure4() {
        return measure4;
    }

    public void setMeasure4(String measure4) {
        this.measure4 = measure4;
    }

    public String getIngredient5() {
        return ingredient5;
    }

    public void setIngredient5(String ingredient5) {
        this.ingredient5 = ingredient5;
    }

    public String getMeasure5() {
        return measure5;
    }

    public void setMeasure5(String measure5) {
        this.measure5 = measure5;
    }

    public String getIngredient6() {
        return ingredient6;
    }

    public void setIngredient6(String ingredient6) {
        this.ingredient6 = ingredient6;
    }

    public String getMeasure6() {
        return measure6;
    }

    public void setMeasure6(String measure6) {
        this.measure6 = measure6;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }


    // -- Parcelable Implementation --
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Cocktail createFromParcel(Parcel in) {
            return new Cocktail(in);
        }

        public Cocktail[] newArray(int size) {
            return new Cocktail[size];
        }
    };

    public Cocktail(Parcel in){
        id = in.readInt();
        name = in.readString();
        imageUrl = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(imageUrl);
    }

    @Override
    public String toString(){
        return "Cocktail{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        return (getId() == ((Cocktail)obj).getId());
    }
}
