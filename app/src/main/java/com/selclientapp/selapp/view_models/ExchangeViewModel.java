package com.selclientapp.selapp.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.selclientapp.selapp.model.Category;
import com.selclientapp.selapp.model.Exchange;
import com.selclientapp.selapp.repositories.ExchangeRepository;
import com.selclientapp.selapp.utils.NumberLimits;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;

public class ExchangeViewModel extends ViewModel {
    private ExchangeRepository exchangeRepository;
    private LiveData<List<Exchange>> allExchanges;
    private LiveData<Exchange> exchangeLiveData;
    private LiveData<List<Category>> categoryList;

    @Inject
    public ExchangeViewModel(ExchangeRepository exchangeRepository) {
        this.exchangeRepository = exchangeRepository;
    }

    public void init(NumberLimits numberLimit) {
        allExchanges = exchangeRepository.getAllExchanges(numberLimit);
    }

    public void AddExchange(Exchange exchange) {
        exchangeLiveData = exchangeRepository.saveExchange(exchange);
    }

    public void deleteOneExchange(int id) {
        exchangeLiveData = exchangeRepository.deleteOneExchange(id);
    }

    public void updateExchange(Exchange exchange) {
        exchangeLiveData = exchangeRepository.updateExchange(exchange);
    }

    public void addCategoryExchange(File file, Category category){
       exchangeRepository.addCategory(file,category);
    }

    public void getAllCategory(){
        categoryList = exchangeRepository.getAllCategory();
    }

    public void deleteCategory(){
        categoryList = exchangeRepository.deleteCategory();
    }

    public void getMyCategories(){
        categoryList = exchangeRepository.getMyCategories();
    }

    public LiveData<List<Category>> getCategoryList() {
        return categoryList;
    }

    public LiveData<List<Exchange>> getAllExchanges() {
        return allExchanges;
    }

    public LiveData<Exchange> getExchangeLiveData() {
        return exchangeLiveData;
    }
}
