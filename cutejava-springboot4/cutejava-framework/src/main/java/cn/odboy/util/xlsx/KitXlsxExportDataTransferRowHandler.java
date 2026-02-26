package cn.odboy.util.xlsx;

import java.util.List;

public interface KitXlsxExportDataTransferRowHandler<O, T> {

  List<T> doHandler(O o);
}
