package com.nirwal.gailconnect.ui.birthdayList;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nirwal.gailconnect.R;
import com.nirwal.gailconnect.adaptors.BirthdayAdaptor;
import com.nirwal.gailconnect.adaptors.IOnListItemClickListener;
import com.nirwal.gailconnect.databinding.FragmentBirthdayBinding;
import com.nirwal.gailconnect.modal.Contact;

import java.util.ArrayList;
import java.util.List;

public class BirthdayFragment extends Fragment implements IOnListItemClickListener
{

    private BirthdayViewModel _viewModel;
    private FragmentBirthdayBinding _viewBinding;
    private BirthdayAdaptor _adaptor;

    public static BirthdayFragment newInstance() {
        return new BirthdayFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        _viewBinding = FragmentBirthdayBinding.inflate(inflater,container, false);
        _viewBinding.imgBackBtn.setOnClickListener(v->{
            Navigation.findNavController(requireView())
                    .popBackStack();
        });

        return _viewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _viewModel = new ViewModelProvider(this).get(BirthdayViewModel.class);
        // TODO: Use the ViewModel

        _adaptor = new BirthdayAdaptor(requireContext(),new ArrayList<>(), this);

        //normal listview
        _viewBinding.birthdayRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        _viewBinding.birthdayRecycler.setAdapter(_adaptor);

        _viewModel.get_birthdayList(requireActivity().getApplication()).observe(getViewLifecycleOwner(), new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                _adaptor.updateDataSet(contacts);
            }
        });
    }



    @Override
    public void onListItemClicked(int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BirthdayWishFragment.ARG_CONTACT,
                _viewModel.get_birthdayList(getActivity().getApplication()).getValue().get(position));
        Navigation.findNavController(requireView())
                .navigate(R.id.action_birthdayFragment_to_birthdayWishFragment,bundle );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _viewModel= null;
        _viewBinding = null;
        _adaptor = null;
    }
}