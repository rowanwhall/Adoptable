package personal.rowan.petfinder.ui.shelter.recycler

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import personal.rowan.petfinder.R
import personal.rowan.petfinder.ui.shelter.ShelterListViewState
import rx.Observable
import rx.subjects.PublishSubject

/**
 * Created by Rowan Hall
 */
class ShelterAdapter(private var mData: List<ShelterListViewState>?) : RecyclerView.Adapter<ShelterViewHolder>() {

    private val mPetsButtonSubject: PublishSubject<Pair<String?, String?>> = PublishSubject.create()
    private val mDirectionsButtonSubject: PublishSubject<String> = PublishSubject.create()

    fun paginateData(data: List<ShelterListViewState>) {
        if(mData == null || mData!!.isEmpty()) {
            mData = data
            notifyDataSetChanged()
        } else {
            val originalSize = itemCount
            mData = data
            notifyItemRangeInserted(originalSize, data.size)
        }
    }

    override fun onBindViewHolder(holder: ShelterViewHolder, position: Int) {
        holder.bind(mData!!.get(position), mPetsButtonSubject, mDirectionsButtonSubject)
    }

    override fun getItemCount(): Int {
        return mData!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShelterViewHolder {
        return ShelterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.listitem_shelter, parent, false))
    }

    fun petsButtonObservable(): Observable<Pair<String?, String?>> {
        return mPetsButtonSubject
    }

    fun directionsButtonObservable(): Observable<String> {
        return mDirectionsButtonSubject
    }
    
}