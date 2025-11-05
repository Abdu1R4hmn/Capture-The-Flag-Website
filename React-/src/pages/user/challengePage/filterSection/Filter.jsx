import React, { useEffect, useState } from 'react';
import './filter.css';
import { useRef } from 'react';
import axios from 'axios';

// Filter component for searching and filtering challenges
function Filter({ onFilter }) {
    // State for selected filters and available categories
    const [selectedCategory, setSelectedCategory] = useState('All');
    const [selectedDifficulty, setSelectedDifficulty] = useState('All');
    const [searchTerm, setSearchTerm] = useState('');
    const [categories, setCategories] = useState([]);

    // Fetch categories from backend on mount
    useEffect(() => {
        axios.get('/api/category/get/all/with-challenge-counts')
            .then(res => setCategories(res.data.response))
            .catch(() => setCategories([]));
    }, []);

    // Notify parent component when filters change
    const applyFilters = (newFilters) => {
        const filters = {
            searchTerm,
            selectedCategory,
            selectedDifficulty,
            ...newFilters,
        };
        onFilter(filters);
    };

    // Debounce utility to limit how often search is triggered
    function debounce(func, delay) {
        let timeout;
        return (...args) => {
            clearTimeout(timeout);
            timeout = setTimeout(() => func(...args), delay);
        };
    }

    // Debounced search handler
    const debouncedSearch = useRef(
        debounce((value) => {
            applyFilters({ searchTerm: value });
        }, 300)
    ).current;

    return (
        <div className="filter-Section">
            {/* Filter header */}
            <div className="filter-header filterParts"><h1>Filter</h1></div>

            {/* Search input */}
            <div className="filter-search filterParts">
                <div className="filter-subheader"><h2>Search</h2></div>
                <input
                    type="text"
                    placeholder='Search by name'
                    value={searchTerm}
                    onChange={(e) => {
                        const value = e.target.value;
                        setSearchTerm(value);
                        debouncedSearch(value);
                    }}
                />
            </div>

            {/* Category filter list */}
            <div className="filter-list filterParts">
                <div className="filter-subheader"><h2>Category</h2></div>
                <div className="filter-list-items">
                    <ul>
                        {/* "All" category */}
                        <li
                            className={selectedCategory === 'All' ? 'activeCell' : ''}
                            onClick={() => {
                                setSelectedCategory('All');
                                applyFilters({ selectedCategory: 'All' });
                            }}
                        >
                            All
                        </li>
                        {/* Dynamic categories from backend */}
                        {categories.map((cat) => (
                            <li
                                key={cat.id}
                                className={selectedCategory === cat.type ? 'activeCell' : ''}
                                onClick={() => {
                                    setSelectedCategory(cat.type);
                                    applyFilters({ selectedCategory: cat.type });
                                }}
                            >
                                {cat.type}
                            </li>
                        ))}
                    </ul>
                </div>
            </div>

            {/* Difficulty filter list */}
            <div className="filter-list filterParts">
                <div className="filter-subheader"><h2>Difficulty</h2></div>
                <div className="filter-list-items">
                    <ul>
                        {['All', 'EASY', 'MEDIUM', 'HARD'].map((level) => (
                            <li
                                key={level}
                                className={selectedDifficulty === level ? 'activeCell' : ''}
                                onClick={() => {
                                    setSelectedDifficulty(level);
                                    applyFilters({ selectedDifficulty: level });
                                }}
                            >
                                {level}
                            </li>
                        ))}
                    </ul>
                </div>
            </div>
        </div>
    );
}

export default Filter;
